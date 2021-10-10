package com.dreampany.word.ml.text

import android.content.Context
import com.dreampany.word.misc.exts.first5
import com.dreampany.word.ml.graphic.GraphicOverlay
import com.dreampany.word.ml.vision.VisionProcessor
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class TextRecognitionProcessor(
    context: Context,
    options: TextRecognizerOptions
) : VisionProcessor<Text>(context) {

    private val recognizer = TextRecognition.getClient(options)
    private val shouldGroupRecognizedTextInBlocks = true
    val texts = HashSet<String>()

    private lateinit var onNewText: (text: String?) -> Unit

    override fun stop() {
        super.stop()
        recognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> = recognizer.process(image)

    override fun onSuccess(result: Text, overlay: GraphicOverlay) {
        //overlay.add(TextGraphic(overlay, result, shouldGroupRecognizedTextInBlocks))
        texts.add(result.text)
        Timber.v("RecognitionProcessor ${result.text}")
        onNewText(result.first5)
    }

    override fun onFailure(error: Throwable) {
        Timber.e(error)
    }

    fun setListener(onNewText: (text: String?) -> Unit) {
        this.onNewText = onNewText
    }
}