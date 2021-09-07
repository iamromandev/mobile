package com.dreampany.hi.ui.vm

import android.app.Application
import com.dreampany.common.misc.exts.deviceId
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
import com.dreampany.common.ui.vm.BaseViewModel
import com.dreampany.device.DeviceInfo
import com.dreampany.hi.app.App
import com.dreampany.hi.data.enums.Action
import com.dreampany.hi.data.enums.State
import com.dreampany.hi.data.enums.Subtype
import com.dreampany.hi.data.enums.Type
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.model.User
import com.dreampany.hi.data.source.pref.Pref
import com.dreampany.hi.data.source.repo.UserRepo
import com.dreampany.hi.manager.NearbyManager
import com.dreampany.hi.ui.model.UserItem
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@HiltViewModel
class UserViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val ex : Executors,
    private val deviceInfo: DeviceInfo,
    private val pref: Pref,
    private val repo: UserRepo
) : BaseViewModel<Type, Subtype, State, Action, User, UserItem, UiTask<Type, Subtype, State, Action, User>>(
    application, rm
), NearbyManager.Callback {

    override fun onUser(user: User, nearby: Boolean, internet: Boolean) {
        Timber.v("%s-nearby[%s]", user.toString(), nearby)

        ex.postToUi {
            postResult(UserItem(user, nearby = nearby))
        }
    }

    override fun onMessage(user: User, message: Message) {
         Timber.v("%s-%s", user.toString(), message.toString())
    }

    fun registerNearby() {
        repo.register(this)
    }

    fun unregisterNearby() {
        repo.unregister(this)
    }


    fun createAnonymousUser() {
        val deviceId = getApplication<App>().deviceId
        val user = User(deviceId)
        user.name = deviceInfo.model
        pref.user = user
    }

    private fun postResult(result: UserItem?) {
        postSingle(
            Type.USER,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            progress = false
        )
    }

}