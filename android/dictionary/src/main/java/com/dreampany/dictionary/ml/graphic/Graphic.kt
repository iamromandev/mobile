package com.dreampany.dictionary.ml.graphic

import android.graphics.Canvas
import android.graphics.Matrix

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class Graphic(private val overlay: GraphicOverlay) {

    abstract fun draw(canvas: Canvas)

    val transformationMatrix: Matrix get() = overlay.transformationMatrix

    fun scale(imagePixel: Float) = imagePixel * overlay.scaleFactor

    fun translateX(x: Float): Float {
        var translate = scale(x) - overlay.postScaleWidthOffset
        if (overlay.isImageFlipped)
            translate = overlay.width - translate
        return translate
    }

    fun translateY(y: Float): Float = scale(y) - overlay.postScaleHeightOffset


    fun postInvalidate() = overlay.postInvalidate()

}