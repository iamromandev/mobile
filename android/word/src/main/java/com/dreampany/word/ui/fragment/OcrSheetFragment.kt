package com.dreampany.word.ui.fragment

import android.graphics.*
import android.os.Bundle
import android.util.Size
import android.view.SurfaceHolder
import android.widget.CompoundButton
import androidx.annotation.ColorInt
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.*
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.word.R
import com.dreampany.word.data.enums.Action
import com.dreampany.word.data.enums.State
import com.dreampany.word.data.enums.Subtype
import com.dreampany.word.data.enums.Type
import com.dreampany.word.databinding.OcrSheetFragmentBinding
import com.dreampany.word.misc.exts.applyLink
import com.dreampany.word.ml.text.TextRecognitionProcessor
import com.dreampany.word.ui.model.WordItem
import com.dreampany.word.ui.vm.WordViewModel
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by roman on 10/6/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class OcrSheetFragment
@Inject constructor(

) : BaseFragment<OcrSheetFragmentBinding>(),
    SurfaceHolder.Callback,
    CompoundButton.OnCheckedChangeListener {

    override val layoutRes: Int = R.layout.ocr_sheet_fragment

    @Transient
    private var inited = false

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var focusCanvas: Canvas
    private lateinit var holder: SurfaceHolder

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var previewUseCase: Preview
    private lateinit var analysisUseCase: ImageAnalysis
    private lateinit var imageProcessor: TextRecognitionProcessor

    private var needUpdateGraphicOverlayImageSourceInfo = false

    private val permissions = arrayOf<Permission>(Permission.CAMERA)

    private lateinit var paint: Paint
    private var boxWidth = 0
    private var boxHeight = 0

    private var analysisPaused = false

    private lateinit var onClose: () -> Unit

    private val vm: WordViewModel by viewModels()

    @ExperimentalGetImage
    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
        if (::onClose.isInitialized)
            onClose()
    }

    @ExperimentalGetImage
    override fun onResume() {
        super.onResume()
        startOcr()
    }

    override fun onPause() {
        super.onPause()
        stopOcr()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        drawFocusRect(color(R.color.colorAccent))
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    @ExperimentalGetImage
    override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
        if (!::cameraProvider.isInitialized) return
        val newLensFacing =
            if (lensFacing == CameraSelector.LENS_FACING_FRONT)
                CameraSelector.LENS_FACING_BACK
            else
                CameraSelector.LENS_FACING_FRONT
        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()
        try {
            if (cameraProvider.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                startOcr()
                return
            }
        } catch (error: CameraInfoUnavailableException) {
            Timber.e(error)
        }
    }

    val texts: List<String>
        get() = imageProcessor.texts.toList()

    fun setListener(onClose: () -> Unit) {
        this.onClose = onClose
    }

    @ExperimentalGetImage
    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true
        val context = contextRef?.applicationContext ?: return false

        binding.buttonRetry.setOnSafeClickListener {
            analysisPaused = false
            binding.buttons.invisible()
        }

        binding.overlay.setZOrderOnTop(true)
        holder = binding.overlay.holder
        holder.setFormat(PixelFormat.TRANSPARENT)
        holder.addCallback(this)

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        //binding.facingSwitch.setOnCheckedChangeListener(this)

        if (!::cameraProvider.isInitialized) {
            val providerFuture = ProcessCameraProvider.getInstance(context)
            providerFuture.addListener(
                {
                    try {
                        cameraProvider = providerFuture.get()
                        startOcr()
                    } catch (error: ExecutionException) {
                        Timber.e(error)
                    } catch (error: InterruptedException) {
                        Timber.e(error)
                    }
                },
                ContextCompat.getMainExecutor(context)
            )
        }

        if (!allPermissionsGranted) checkPermissions()

        vm.subscribe(this, { this.processResponse(it) })
        return true
    }

    @ExperimentalGetImage
    private fun startOcr() {
        if (allPermissionsGranted) bindAllCameraUseCases()
    }

    private fun stopOcr() {
        if (::imageProcessor.isInitialized) imageProcessor.stop()
    }

    private fun onClickedText(text: String) {
        Timber.v(text)
        vm.read(text)
    }

    private fun drawFocusRect(@ColorInt color: Int) {
        val width = binding.preview.width
        val height = binding.preview.height

        Timber.v("drawFocusRect width $width height $height")

        var diameter = width
        if (height < width) diameter = height

        val offset: Int = (0.05 * diameter).toInt()
        diameter -= offset

        focusCanvas = holder.lockCanvas()
        focusCanvas.drawColor(0, PorterDuff.Mode.CLEAR)

        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = color
        paint.strokeWidth = 5f

        val left = width / 2 - diameter / 3
        val top = height / 2 - diameter / 3
        val right = width / 2 + diameter / 3
        val bottom = height / 2 + diameter / 3

        boxWidth = right - left
        boxHeight = bottom - top

        Timber.v("boxWidth $boxWidth boxHeight $boxHeight")

        focusCanvas.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            paint
        )
        holder.unlockCanvasAndPost(focusCanvas)
    }

    @ExperimentalGetImage
    private fun bindAllCameraUseCases() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (!::cameraProvider.isInitialized) return
        if (::previewUseCase.isInitialized) cameraProvider.unbind(previewUseCase)

        val builder = Preview.Builder()
        val targetResolution = Size.parseSize("700x800")
        builder.setTargetResolution(targetResolution)

        previewUseCase = builder.build()
        previewUseCase.setSurfaceProvider(binding.preview.surfaceProvider)
        cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase)
    }

    @ExperimentalGetImage
    private fun bindAnalysisUseCase() {
        if (!::cameraProvider.isInitialized) return
        if (::analysisUseCase.isInitialized) cameraProvider.unbind(analysisUseCase)
        if (::imageProcessor.isInitialized) imageProcessor.stop()

        val context = contextRef ?: return

        imageProcessor = TextRecognitionProcessor(context, TextRecognizerOptions.Builder().build())
        imageProcessor.setListener { text ->
            if (!analysisPaused) {
                binding.text.text = text
                text?.split("  ")?.forEach {
                    binding.text.applyLink(R.color.white, it) { onClickedText(it) }
                }
            }
            if (!text.isNullOrEmpty()) {
                binding.buttons.show()
                analysisPaused = true
            }
        }

        val builder = ImageAnalysis.Builder()
        val targetResolution = Size.parseSize("700x800")
        builder.setTargetResolution(targetResolution)
        builder.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)

        analysisUseCase = builder.build()
        needUpdateGraphicOverlayImageSourceInfo = true

        var analyzed = 0
        analysisUseCase.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            { proxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    needUpdateGraphicOverlayImageSourceInfo = false
                    val isFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                    val degree = proxy.imageInfo.rotationDegrees
                    if (degree == 0 || degree == 180)
                        binding.overlay.setImageSourceInfo(proxy.width, proxy.height, isFlipped)
                    else
                        binding.overlay.setImageSourceInfo(proxy.height, proxy.width, isFlipped)
                }
                if (analyzed++ % 10 == 0) {
                    proxy.close()
                    return@setAnalyzer
                }
                try {
                    imageProcessor.processImageProxy(
                        proxy,
                        binding.overlay,
                        binding.preview.width,
                        binding.preview.height,
                        boxWidth,
                        boxHeight
                    )
                } catch (error: MlKitException) {
                    Timber.e(error)
                }
            }
        )
        cameraProvider.bindToLifecycle(this, cameraSelector, analysisUseCase)
    }

    @ExperimentalGetImage
    private fun checkPermissions() {
        runWithPermissions(*permissions) {
            startOcr()
        }
    }

    private val allPermissionsGranted: Boolean
        get() {
            for (permission in permissions)
                if (!contextRef.hasPermission(permission.value)) return false
            return true
        }

    private fun processResponse(response: Response<Type, Subtype, State, Action, WordItem>) {
        if (response is Response.Progress) {
            applyProgress(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, WordItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: WordItem?) {
        if (result != null) {
            binding.word.text = result.input.word
            binding.pronunciation.text = result.pronunciation
            binding.definition.text = result.definition.html
            binding.layoutWord.visible()
        }
    }

}