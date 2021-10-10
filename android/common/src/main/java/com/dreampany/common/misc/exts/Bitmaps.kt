package com.dreampany.common.misc.exts

import android.graphics.Bitmap
import android.media.Image

/**
 * Created by roman on 10/10/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
/*
val Image.bitmap: Bitmap
    get() {
        val planes = planes
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        uBuffer.get(nv21, ySize, ySize)
    }*/
