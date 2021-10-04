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
import com.dreampany.word.data.source.repo.WordRepo
import com.dreampany.word.ui.model.WordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val repo: WordRepo
) : BaseViewModel<Type, Subtype, State, Action, Word, WordItem<*>, UiTask<Type, Subtype, State, Action, Word>>(
    application, rm
) {

    fun read(word: String) {
        uiScope.launch {
            var result: Word? = null
            var errors: SmartError? = null
            try {
                result = repo.read(word)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                //postError(errors)
            } else {
                //postResult(result?.toItems())
            }
        }
    }
}