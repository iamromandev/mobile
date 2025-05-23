package com.dreampany.dictionary.ml.vision

import android.graphics.Bitmap
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.dreampany.dictionary.ml.graphic.GraphicOverlay
import com.google.mlkit.common.MlKitException
import java.nio.ByteBuffer

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface VisionImageProcessor {
    fun processBitmap(bitmap: Bitmap, overlay: GraphicOverlay)

    @Throws(MlKitException::class)
    fun processByteBuffer(data: ByteBuffer, frameMeta: FrameMeta, overlay: GraphicOverlay)

    @RequiresApi(VERSION_CODES.KITKAT)
    @Throws(MlKitException::class)
    fun processImageProxy(imageProxy: ImageProxy, overlay: GraphicOverlay)

    @RequiresApi(VERSION_CODES.KITKAT)
    @Throws(MlKitException::class)
    fun processImageProxy(
        imageProxy: ImageProxy,
        overlay: GraphicOverlay,
        baseWidth:Int,
        baseHeight:Int,
        boxWidth: Int,
        boxHeight: Int
    )

    fun stop()
}