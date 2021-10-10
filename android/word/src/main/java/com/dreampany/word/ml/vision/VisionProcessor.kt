package com.dreampany.word.ml.vision

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import androidx.annotation.GuardedBy
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.dreampany.word.misc.exts.bitmap
import com.dreampany.word.ml.graphic.CameraImageGraphic
import com.dreampany.word.ml.graphic.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.lang.Long.max
import java.lang.Long.min
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class VisionProcessor<T>(context: Context) : VisionImageProcessor {

    private var activityManager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val fpsTimer = Timer()
    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

    private var isShutdown: Boolean = false
    private var numRuns: Int = 0
    private var totalFrameMs: Long = 0L
    private var maxFrameMs = 0L
    private var minFrameMs = Long.MAX_VALUE
    private var totalDetectorMs = 0L
    private var maxDetectorMs = 0L
    private var minDetectorMs = Long.MAX_VALUE

    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

    @GuardedBy("this")
    private var latestImage: ByteBuffer? = null

    @GuardedBy("this")
    private var latestImageMeta: FrameMeta? = null

    @GuardedBy("this")
    private var processingImage: ByteBuffer? = null

    @GuardedBy("this")
    private var processingMeta: FrameMeta? = null

    init {
        fpsTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    framesPerSecond = frameProcessedInOneSecondInterval
                    frameProcessedInOneSecondInterval = 0
                }
            },
            0,
            1000
        )
    }

    protected abstract fun detectInImage(image: InputImage): Task<T>

    protected abstract fun onSuccess(result: T, overlay: GraphicOverlay)

    protected abstract fun onFailure(error: Throwable)

    override fun processBitmap(bitmap: Bitmap, overlay: GraphicOverlay) {
        val frameStartMs = SystemClock.elapsedRealtime()

    }

    override fun processByteBuffer(data: ByteBuffer, frameMeta: FrameMeta, overlay: GraphicOverlay) {
        latestImage = data
        latestImageMeta = frameMeta
        if (processingImage == null && processingMeta == null)
            processLatestImage(overlay)
    }

    @ExperimentalGetImage
    override fun processImageProxy(imageProxy: ImageProxy, overlay: GraphicOverlay) {
        val frameStartMs = SystemClock.elapsedRealtime()
        if (isShutdown) return
        val image = imageProxy.image ?: return
        val imageBitmap = imageProxy.bitmap ?: return
        val bitmap = null

        val width = imageBitmap.width
        val height = imageBitmap.height

        var diameter = width
        if (height < width) diameter = height

        val offset = (0.05 * diameter).toInt()
        diameter -= offset

        val left = width / 2 - diameter / 3
        val top = height / 4 - diameter / 8
        val right = width / 2 + diameter / 3
        val bottom = height / 4 + diameter / 8

        val boxWidth = right - left
        val boxHeight = bottom - top

        val cropBitmap: Bitmap = Bitmap.createBitmap(imageBitmap, left, top, boxWidth, boxHeight)

        requestDetectInImage(
            InputImage.fromBitmap(cropBitmap, imageProxy.imageInfo.rotationDegrees),
            overlay,
            bitmap,
            frameStartMs,
            false
        ).addOnCompleteListener { imageProxy.close() }
    }

    override fun stop() {
        executor.shutdown()
        isShutdown = true
        resetLatencyStats()
        fpsTimer.cancel()
    }

    @Synchronized
    private fun processLatestImage(overlay: GraphicOverlay) {
        processingImage = latestImage
        processingMeta = latestImageMeta

        latestImage = null
        latestImageMeta = null

        if (processingImage != null && processingMeta != null && !isShutdown)
            processImage(processingImage!!, processingMeta!!, overlay)
    }

    private fun processImage(data: ByteBuffer, meta: FrameMeta, overlay: GraphicOverlay) {
        val frameStartMs = SystemClock.elapsedRealtime()
        val bitmap = null//BitmapUtils.getBitmap(data, meta)

        requestDetectInImage(
            InputImage.fromByteBuffer(
                data,
                meta.width,
                meta.height,
                meta.rotation,
                InputImage.IMAGE_FORMAT_NV21
            ),
            overlay,
            bitmap,
            frameStartMs,
            true
        )
    }

    private fun requestDetectInImage(
        image: InputImage,
        overlay: GraphicOverlay,
        bitmap: Bitmap?,
        frameStartMs: Long,
        shouldShowFps: Boolean
    ): Task<T> {
        return setupListener(
            detectInImage(image),
            overlay,
            bitmap,
            frameStartMs,
            shouldShowFps
        )
    }

    private fun setupListener(
        task: Task<T>,
        overlay: GraphicOverlay,
        bitmap: Bitmap?,
        frameStartMs: Long,
        shouldShowFps: Boolean
    ): Task<T> {
        val detectorStartMs = SystemClock.elapsedRealtime()
        return task
            .addOnSuccessListener(executor, { result: T ->
                onSuccess(result, overlay, bitmap, frameStartMs, detectorStartMs)
            }).addOnFailureListener(executor, { error: Throwable ->
                onFailure(error, overlay)
            })
    }

    private fun onSuccess(
        result: T,
        overlay: GraphicOverlay,
        bitmap: Bitmap?,
        frameStartMs: Long,
        detectorStartMs: Long
    ) {
        val detectorEndMs = SystemClock.elapsedRealtime()
        val currentFrameLatencyMs = detectorEndMs - frameStartMs
        val currentDetectorLatencyMs = detectorEndMs - detectorStartMs

        if (numRuns >= 500)
            resetLatencyStats()

        numRuns++
        frameProcessedInOneSecondInterval++

        totalFrameMs += currentFrameLatencyMs
        maxFrameMs = max(currentFrameLatencyMs, maxFrameMs)
        minFrameMs = min(currentFrameLatencyMs, minFrameMs)

        totalDetectorMs += currentDetectorLatencyMs
        maxDetectorMs = Math.max(currentDetectorLatencyMs, maxDetectorMs)
        minDetectorMs = Math.min(currentDetectorLatencyMs, minDetectorMs)

        if (frameProcessedInOneSecondInterval == 1) {
            val mi = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(mi)
            val availableMegs: Long = mi.availMem / 0x100000L
            Timber.d("Memory available in system: $availableMegs MB")
        }

        overlay.clear()
        bitmap?.let { overlay.add(CameraImageGraphic(overlay, it)) }
        onSuccess(result, overlay)
        overlay.postInvalidate()
    }

    private fun onFailure(error: Throwable, overlay: GraphicOverlay) {
        overlay.clear()
        overlay.postInvalidate()
        Timber.e(error)
        error.printStackTrace()
        onFailure(error)
    }

    private fun resetLatencyStats() {
        numRuns = 0
        totalFrameMs = 0
        maxFrameMs = 0
        minFrameMs = Long.MAX_VALUE
        totalDetectorMs = 0
        maxDetectorMs = 0
        minDetectorMs = Long.MAX_VALUE
    }
}