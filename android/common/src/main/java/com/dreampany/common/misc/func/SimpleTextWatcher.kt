package com.dreampany.common.misc.func

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by roman on 10/16/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class SimpleTextWatcher : TextWatcher {
    override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(text: Editable) {
    }
}