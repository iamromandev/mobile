package com.dreampany.dictionary.ml.graphic

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Created by roman on 10/8/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class CameraImageGraphic(
    private val overlay: GraphicOverlay,
    private val bitmap: Bitmap
) : Graphic(overlay) {

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, overlay.transformationMatrix, null)
    }
}