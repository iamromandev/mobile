package com.dreampany.hi.ui.vm

import android.app.Application
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.data.source.repo.MessageRepo
import com.dreampany.hi.manager.NearbyManager
import com.dreampany.hi.ui.model.InTextMessageItem
import com.dreampany.hi.ui.model.MessageItem
import com.dreampany.hi.ui.model.OutTextMessageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/19/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class MessageViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val ex: Executors,
    private val pref: Pref,
    private val repo: MessageRepo
) : BaseViewModel<Type, Subtype, State, Action, Message, MessageItem<*>, UiTask<Type, Subtype, State, Action, Message>>(
    application, rm
), NearbyManager.Callback {

    private lateinit var user: User
    private var currentPage: Int = 1
    private var pageSize: Int = 10

    override fun onUser(user: User, nearby: Boolean, internet: Boolean) {

    }

    override fun onMessage(user: User, message: Message) {
        Timber.v("%s-%s", user, message)
        uiScope.launch {
            repo.write(message)
        }
        ex.postToUi {
            postResult(InTextMessageItem(message))
        }
    }

    fun registerFlow(action: suspend (value: PagingData<Message>) -> Unit) {
        uiScope.launch {
            repo.readsByFlow(PagingConfig(pageSize = 10, enablePlaceholders = true))
                .collectLatest(action)
        }
    }

    fun registerNearby(user: User) {
        this.user = user
        repo.register(this)
    }

    fun unregisterNearby() = repo.unregister(this)

    fun reads() {
        uiScope.launch {
            var result: List<Message>? = null
            var errors: SmartError? = null
            try {
                result = repo.reads(currentPage++, pageSize)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun sendTextMessage(target: User, message: Message) {
        uiScope.launch {
            repo.send(target, message)
        }
    }

    private suspend fun List<Message>.toItems(): List<MessageItem<*>> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                if (user.id == input.author)
                    OutTextMessageItem(input)
                else
                    InTextMessageItem(input)
            }
        }
    }

    private fun postError(error: SmartError) {
        postMultiple(
            Type.MESSAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            progress = true
        )
    }

    private fun postResult(result: MessageItem<*>) {
        postSingle(
            Type.MESSAGE,
            Subtype.TEXT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            progress = false
        )
    }

    private fun postResult(result: List<MessageItem<*>>?) {
        postMultiple(
            Type.MESSAGE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            progress = false
        )
    }
}