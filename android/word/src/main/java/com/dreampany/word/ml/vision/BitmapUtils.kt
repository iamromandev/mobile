package com.dreampany.word.ml.vision

import android.graphics.*
import com.dreampany.common.misc.exts.length
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Created by roman on 10/8/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class BitmapUtils {
    companion object {
        fun getBitmap(data: ByteBuffer, meta: FrameMeta): Bitmap? {
            data.rewind()
            val imageInBuffer = ByteArray(data.limit())
            data.get(imageInBuffer, 0, imageInBuffer.length)
            try {
                val image = YuvImage(imageInBuffer, ImageFormat.NV21, meta.width, meta.height, null)
                val stream = ByteArrayOutputStream()
                image.compressToJpeg(Rect(0, 0, meta.width, meta.height), 80, stream)
                val bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
                stream.close()
                return rotate(bitmap, meta.rotation, false, false)
            } catch (error: Throwable) {
                Timber.e(error)
            }
            return null
        }

        fun rotate(bitmap: Bitmap, degree: Int, flipX: Boolean, flipY: Boolean): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            matrix.postScale(if (flipX) -1.0f else 1.0f, if (flipY) -1.0f else 1.0f)
            val rotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (rotated != bitmap) bitmap.recycle()
            return rotated
        }
    }
}