package com.dreampany.hi.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.currentMillis
import com.dreampany.common.misc.exts.randomId
import com.dreampany.common.misc.exts.task
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.common.ui.model.UiTask
import com.dreampany.hi.R
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.databinding.MessageActivityBinding
import com.dreampany.hi.ui.adapter.MessageAdapter
import com.dreampany.hi.ui.fragment.FileSheetFragment
import com.dreampany.hi.ui.model.MessageItem
import com.dreampany.hi.ui.model.OutTextMessageItem
import com.dreampany.hi.ui.vm.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/18/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class MessageActivity : BaseActivity<MessageActivityBinding>() {

    @Inject
    internal lateinit var pref: Pref

    @Inject
    internal lateinit var fileSheet: FileSheetFragment

    override val layoutRes: Int = R.layout.message_activity
    override val toolbarId: Int = R.id.toolbar

    private val vm: MessageViewModel by viewModels()
    private lateinit var adapter: MessageAdapter

    private lateinit var user: User
    private lateinit var target: User

    @Transient
    private var inited = false
    private var count = 0

    override fun onStartUi(state: Bundle?) {
        user = pref.user ?: return
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, User>
        target = task.input ?: return

        inited = initUi(state)
    }

    override fun onStopUi() {
        vm.unregisterNearby()
    }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true
        binding.name.text = user.name
        binding.send.setOnClickListener {
            //send100RandomMessage()
            showFileSheet()
            sendTextMessage(binding.edit.text.toString())
            binding.edit.text = null
            //send100RandomMessage()
        }

        adapter = MessageAdapter(
            { currentPage ->
                Timber.v("onLoadMore")
                vm.reads()
            }, this::onItemPressed
        )

        adapter.initRecycler(
            state,
            binding.recycler,
            reverse = true
        )

        vm.subscribe(this, { this.processResponse(it) })
        vm.subscribes(this, { this.processResponses(it) })
        vm.registerNearby(user)
        vm.reads()
        return true
    }

    private fun onItemPressed(view: View, input: MessageItem<*>) {
        //Timber.v(input.input.name)
        //openMessageUi(input.input)
    }

    private fun showFileSheet() {
        fileSheet.showNow(supportFragmentManager, fileSheet.tag)
    }

    private fun send100RandomMessage() {
        repeat(100) {
            sendRandomMessage()
        }
    }

    private fun sendRandomMessage() {
        count = count.inc()
        val message = Message(
            id = randomId,
            createdAt = currentMillis,
            author = user.id,
            text = "Hello ${count}"
        )
        processResult(OutTextMessageItem(message))
        vm.sendTextMessage(target, message)
    }

    private fun sendTextMessage(text: String) {
        if (text.isEmpty()) return
        count = count.inc()
        val message = Message(
            id = randomId,
            createdAt = currentMillis,
            author = user.id,
            text = text
        )
        processResult(OutTextMessageItem(message))
        vm.sendTextMessage(target, message)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, MessageItem<*>>) {
        if (response is Response.Progress) {
            //binding.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, MessageItem<*>>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<MessageItem<*>>>) {
        if (response is Response.Progress) {

        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<MessageItem<*>>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResult(result: MessageItem<*>?) {
        if (result != null) {
            adapter.addItem(binding.recycler, result)
        }

        if (adapter.isEmpty) {
            //binding.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            //binding.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processResults(result: List<MessageItem<*>>?) {
        adapter.addItems(binding.recycler, result)
    }
}