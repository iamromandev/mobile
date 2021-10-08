package com.dreampany.word.ml.text

import android.content.Context
import com.dreampany.word.ml.graphic.GraphicOverlay
import com.dreampany.word.ml.vision.VisionProcessor
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class TextRecognitionProcessor(private val context: Context, options: TextRecognizerOptions) :
    VisionProcessor<Text>(context) {

    private val recognizer = TextRecognition.getClient(options)
    private val shouldGroupRecognizedTextInBlocks = true

    override fun stop() {
        super.stop()
        recognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> = recognizer.process(image)

    override fun onSuccess(result: Text, overlay: GraphicOverlay) {
        overlay.add(TextGraphic(overlay, result, shouldGroupRecognizedTextInBlocks))
    }

    override fun onFailure(error: Throwable) {
    }


}