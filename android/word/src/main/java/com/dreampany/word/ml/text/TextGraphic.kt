package com.dreampany.word.ml.text

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.dreampany.word.ml.graphic.Graphic
import com.dreampany.word.ml.graphic.GraphicOverlay
import com.google.mlkit.vision.text.Text
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class TextGraphic
constructor(
    overlay: GraphicOverlay,
    private val text: Text,
    private val shouldGroupTextInBlocks: Boolean
) : Graphic(overlay) {

    companion object {
        private const val TAG = "TextGraphic"
        private const val TEXT_COLOR = Color.BLACK
        private const val MARKER_COLOR = Color.WHITE
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
    }

    private val rectPaint: Paint
    private val textPaint: Paint
    private val labelPaint: Paint

    init {
        rectPaint = Paint()
        rectPaint.color = MARKER_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH

        textPaint = Paint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = TEXT_SIZE

        labelPaint = Paint()
        labelPaint.color = MARKER_COLOR
        labelPaint.style = Paint.Style.FILL

        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        Timber.v("Text is: ${text.text}")
        for (textBlock in text.textBlocks) {
            if (shouldGroupTextInBlocks) {
                drawText(
                    textBlock.text,
                    RectF(textBlock.boundingBox),
                    TEXT_SIZE * textBlock.lines.size + 2 * STROKE_WIDTH,
                    canvas
                )
            } else {
                for (line in textBlock.lines) {
                    drawText(
                        line.text,
                        RectF(line.boundingBox),
                        TEXT_SIZE + 2 * STROKE_WIDTH,
                        canvas
                    )
                }
            }
        }
    }

    private fun drawText(text: String, rect: RectF, textHeight: Float, canvas: Canvas) {
        val x0 = translateX(rect.left)
        val x1 = translateX(rect.right)

        rect.left = min(x0, x1)
        rect.right = max(x0, x1)

        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)

        canvas.drawRect(rect, rectPaint)

        val textWidth = textPaint.measureText(text)
        canvas.drawRect(
            rect.left - STROKE_WIDTH,
            rect.top - textHeight,
            rect.left + textWidth + 2 * STROKE_WIDTH,
            rect.top,
            labelPaint
        )
        canvas.drawText(text, rect.left, rect.top - STROKE_WIDTH, textPaint)
    }
}