package com.dreampany.common.misc.func

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by roman on 12/10/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class TtsService
@Inject constructor(
    @ApplicationContext val context: Context
) {

    private var tts: TextToSpeech? = null

    private fun initTts(context: Context) {
        if (tts == null) {
            try {
                tts = TextToSpeech(context) { status ->
                    if (status != TextToSpeech.ERROR) {
                        tts!!.setLanguage(Locale.ENGLISH)
                    }
                }
            } catch (e: IllegalArgumentException) {
                Timber.e("Error in tts: %s", e.toString())
            }

        }
    }

    fun speak(text: String? = null) {
        initTts(context)
        if (tts != null && text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val params = Bundle()
                params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1f)
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                val params = HashMap<String, String>()
                params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1")
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }
}