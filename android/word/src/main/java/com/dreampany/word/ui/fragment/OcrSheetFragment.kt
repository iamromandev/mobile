package com.dreampany.word.ui.fragment

import android.os.Bundle
import android.util.Size
import android.widget.CompoundButton
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.misc.exts.contextRef
import com.dreampany.common.misc.exts.hasPermission
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.word.R
import com.dreampany.word.databinding.OcrSheetFragmentBinding
import com.dreampany.word.ml.text.TextRecognitionProcessor
import com.dreampany.word.ml.vision.VisionImageProcessor
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.ExecutionException
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
    CompoundButton.OnCheckedChangeListener {

    override val layoutRes: Int =  R.layout.ocr_sheet_fragment

    @Transient
    private var inited = false

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var previewUseCase: Preview
    private lateinit var analysisUseCase: ImageAnalysis
    private lateinit var imageProcessor: VisionImageProcessor

    private var needUpdateGraphicOverlayImageSourceInfo = false

    private val permissions = arrayOf<Permission>(Permission.CAMERA)

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
        if (::imageProcessor.isInitialized) imageProcessor.stop()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted) bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        if (::imageProcessor.isInitialized) imageProcessor.stop()
    }

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
                bindAllCameraUseCases()
                return
            }
        } catch (error: CameraInfoUnavailableException) {

        }
    }

    private fun initUi(state: Bundle?): Boolean {
        val context = contextRef ?: return false
        if (inited) return true

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        binding.facingSwitch.setOnCheckedChangeListener(this)

        if (!::cameraProvider.isInitialized) {
            val providerFuture = ProcessCameraProvider.getInstance(context.applicationContext)
            providerFuture.addListener(
                {
                    try {
                        cameraProvider = providerFuture.get()
                        if (allPermissionsGranted) bindAllCameraUseCases()
                    } catch (error: ExecutionException) {
                        Timber.e(error)
                    } catch (error: InterruptedException) {
                        Timber.e(error)
                    }
                },
                ContextCompat.getMainExecutor(context.applicationContext)
            )
        }

        if (!allPermissionsGranted) checkPermissions()
        return true
    }

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
        val targetResolution = Size.parseSize("1000x1000")
        builder.setTargetResolution(targetResolution)
        previewUseCase = builder.build()
        previewUseCase.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase)
    }

    private fun bindAnalysisUseCase() {
        if (!::cameraProvider.isInitialized) return
        if (::analysisUseCase.isInitialized) cameraProvider.unbind(analysisUseCase)
        if (::imageProcessor.isInitialized) imageProcessor.stop()

        val context = contextRef ?: return
        imageProcessor = TextRecognitionProcessor(context, TextRecognizerOptions.Builder().build())
        val builder = ImageAnalysis.Builder()
        val targetResolution = Size.parseSize("1000x1000")
        builder.setTargetResolution(targetResolution)
        analysisUseCase = builder.build()
        needUpdateGraphicOverlayImageSourceInfo = true

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
                try {
                    imageProcessor.processImageProxy(proxy, binding.overlay)
                } catch (error: MlKitException) {
                    Timber.e(error)
                }
            }
        )
        cameraProvider.bindToLifecycle(this, cameraSelector, analysisUseCase)
    }

    private fun checkPermissions() {
        runWithPermissions(*permissions) {
            bindAllCameraUseCases()
        }

    }

    private val allPermissionsGranted: Boolean
        get() {
            val context = contextRef
            for (permission in permissions)
                if (!context.hasPermission(permission.value)) return false
            return true
        }
}