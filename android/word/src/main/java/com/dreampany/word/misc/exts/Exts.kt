package com.dreampany.word.misc.exts

import android.graphics.*
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

/**
 * Created by roman on 10/10/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
val ImageProxy.bitmap: Bitmap?
    get() {
        val nv21 = buffer
        val yuv = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        return yuv.bitmap
    }

val ImageProxy.buffer: ByteArray
    get() {
        val pixelCount = cropRect.width() * cropRect.height()
        val pixelSizeBits = ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888)
        val buffer = ByteArray(pixelCount * pixelSizeBits)
        this.apply(buffer, pixelCount)
        return buffer
    }

val YuvImage.bitmap: Bitmap?
    get() {
        val out = ByteArrayOutputStream()
        if (!compressToJpeg(Rect(0, 0, width, height), 100, out)) return null
        val buffer = out.toByteArray()
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.size)
    }


fun ImageProxy.apply(buffer: ByteArray, pixelCount: Int) {
    assert(this.format == ImageFormat.YUV_420_888)

    val crop = this.cropRect
    val planes = this.planes

    planes.forEachIndexed { index, plane ->
        // How many values are read in input for each output value written
        // Only the Y plane has a value for every pixel, U and V have half the resolution i.e.
        //
        // Y Plane            U Plane    V Plane
        // ===============    =======    =======
        // Y Y Y Y Y Y Y Y    U U U U    V V V V
        // Y Y Y Y Y Y Y Y    U U U U    V V V V
        // Y Y Y Y Y Y Y Y    U U U U    V V V V
        // Y Y Y Y Y Y Y Y    U U U U    V V V V
        // Y Y Y Y Y Y Y Y
        // Y Y Y Y Y Y Y Y
        // Y Y Y Y Y Y Y Y
        val outputStride: Int

        // The index in the output buffer the next value will be written at
        // For Y it's zero, for U and V we start at the end of Y and interleave them i.e.
        //
        // First chunk        Second chunk
        // ===============    ===============
        // Y Y Y Y Y Y Y Y    V U V U V U V U
        // Y Y Y Y Y Y Y Y    V U V U V U V U
        // Y Y Y Y Y Y Y Y    V U V U V U V U
        // Y Y Y Y Y Y Y Y    V U V U V U V U
        // Y Y Y Y Y Y Y Y
        // Y Y Y Y Y Y Y Y
        // Y Y Y Y Y Y Y Y
        var outputOffset: Int

        when (index) {
            0 -> {
                outputStride = 1
                outputOffset = 0
            }
            1 -> {
                outputStride = 2
                // For NV21 format, U is in odd-numbered indices
                outputOffset = pixelCount + 1
            }
            2 -> {
                outputStride = 2
                // For NV21 format, V is in even-numbered indices
                outputOffset = pixelCount
            }
            else -> {
                // Image contains more than 3 planes, something strange is going on
                return@forEachIndexed
            }
        }

        val planeBuffer = plane.buffer
        val rowStride = plane.rowStride
        val pixelStride = plane.pixelStride

        val planeCrop = if (index == 0) {
            crop
        } else {
            Rect(
                crop.left / 2,
                crop.top / 2,
                crop.right / 2,
                crop.bottom / 2
            )
        }

        val planeWidth = planeCrop.width()
        val planeHeight = planeCrop.height()

        // Intermediate buffer used to store the bytes of each row
        val rowBuffer = ByteArray(plane.rowStride)

        // Size of each row in bytes
        val rowLength = if (pixelStride == 1 && outputStride == 1) {
            planeWidth
        } else {
            // Take into account that the stride may include data from pixels other than this
            // particular plane and row, and that could be between pixels and not after every
            // pixel:
            //
            // |---- Pixel stride ----|                    Row ends here --> |
            // | Pixel 1 | Other Data | Pixel 2 | Other Data | ... | Pixel N |
            //
            // We need to get (N-1) * (pixel stride bytes) per row + 1 byte for the last pixel
            (planeWidth - 1) * pixelStride + 1
        }

        for (row in 0 until planeHeight) {
            // Move buffer position to the beginning of this row
            planeBuffer.position(
                (row + planeCrop.top) * rowStride + planeCrop.left * pixelStride)
            if (pixelStride == 1 && outputStride == 1) {
                // When there is a single stride value for pixel and output, we can just copy
                // the entire row in a single step
                planeBuffer.get(buffer, outputOffset, rowLength)
                outputOffset += rowLength
            } else {
                // When either pixel or output have a stride > 1 we must copy pixel by pixel
                planeBuffer.get(rowBuffer, 0, rowLength)
                for (col in 0 until planeWidth) {
                    buffer[outputOffset] = rowBuffer[col * pixelStride]
                    outputOffset += outputStride
                }
            }
        }
    }
}