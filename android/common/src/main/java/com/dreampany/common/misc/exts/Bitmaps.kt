package com.dreampany.common.misc.exts

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Created by roman on 10/10/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
fun Bitmap.resize(newWidth: Int, newHeight: Int) : Bitmap {
    val width = width
    val height = height

    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)

    val resizedBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    this.recycle()
    return resizedBitmap
}
