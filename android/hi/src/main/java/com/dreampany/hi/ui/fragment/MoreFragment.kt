package com.dreampany.hi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.init
import com.dreampany.common.misc.exts.open
import com.dreampany.common.misc.exts.refresh
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.ui.misc.StatefulLayout
import com.dreampany.common.ui.model.UiTask
import com.dreampany.hi.R
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.User
import com.dreampany.hi.databinding.HomeFragmentBinding
import com.dreampany.hi.ui.activity.MessageActivity
import com.dreampany.hi.ui.adapter.UserAdapter
import com.dreampany.hi.ui.model.UserItem
import com.dreampany.hi.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/12/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class MoreFragment @Inject constructor(): BaseFragment<HomeFragmentBinding>() {

    private val vm: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    @Transient
    private var inited = false

    override val layoutRes: Int = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
        vm.unregisterNearby()
    }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true

        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            vm.registerNearby()
        }

        adapter = UserAdapter(
            { currentPage ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, this::onItemPressed
        )

         adapter.initRecycler(
            state,
            binding.recycler
        )

        binding.swipe.init(this)
        //binding.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)

        vm.subscribe(this, { this.processResponse(it) })

        return true
    }

    private fun onItemPressed(view: View, input: UserItem) {
        Timber.v(input.input.name)
        openMessageUi(input.input)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, UserItem>) {
        if (response is Response.Progress) {
            binding.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, UserItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: UserItem?) {
        if (result != null) {
            adapter.addItem(result)
        }

        if (adapter.isEmpty) {
            binding.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            binding.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun openMessageUi(input: User) {
        val task = UiTask(
            Type.USER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            input
        )
        open(MessageActivity::class, task)
    }
}