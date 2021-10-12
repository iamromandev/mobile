package com.dreampany.word.ui.vm

import android.app.Application
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.word.data.enums.Action
import com.dreampany.word.data.enums.State
import com.dreampany.word.data.enums.Subtype
import com.dreampany.word.data.enums.Type
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.repo.DictionaryRepo
import com.dreampany.word.ui.model.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class WordViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val repo: DictionaryRepo
) : BaseViewModel<Type, Subtype, State, Action, Word, WordItem, UiTask<Type, Subtype, State, Action, Word>>(
    application, rm
) {

    fun read(word: String) {
        uiScope.launch {
            postProgress(true)
            var result: Word? = null
            var errors: SmartError? = null
            try {
                result = repo.read(word)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem())
            }
        }
    }

    private suspend fun Word.toItem(): WordItem {
        val input = this
        return withContext(Dispatchers.IO) {
            WordItem(input)
        }
    }

    private fun postProgress(progress: Boolean) {
        postSingle(
            Type.WORD,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postError(error: SmartError) {
        postSingle(
            Type.WORD,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            progress = true
        )
    }

    private fun postResult(result: WordItem?) {
        postSingle(
            Type.WORD,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            progress = true
        )
    }
}