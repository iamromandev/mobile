package com.dreampany.hello.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.hello.data.model.Auth
import com.dreampany.hello.data.model.User
import com.dreampany.hello.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 28/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.PREF

    fun start() {
        setPrivately(Constants.Keys.Pref.STARTED, true)
    }

    fun stop() {
        setPrivately(Constants.Keys.Pref.STARTED, false)
    }

    val isStarted: Boolean
        get() = getPrivately(Constants.Keys.Pref.STARTED, false)

    fun login() {
        setPrivately(Constants.Keys.Pref.LOGGED, true)
    }

    fun logout() {
        setPrivately(Constants.Keys.Pref.LOGGED, false)
    }

    val isLogged: Boolean
        get() = getPrivately(Constants.Keys.Pref.LOGGED, false)

    fun signIn() {
        setPrivately(Constants.Keys.Pref.SIGN_IN, true)
    }

    fun signOut() {
        setPrivately(Constants.Keys.Pref.SIGN_IN, false)
    }

    val isSignIn: Boolean
        get() = getPrivately(Constants.Keys.Pref.SIGN_IN, false)

    fun write(input: Auth) {
        setPrivately(Constants.Keys.Pref.AUTH, input)
    }

    val auth: Auth?
        get() = getPrivately(Constants.Keys.Pref.AUTH, Auth::class.java, null)

    fun write(input: User) {
        setPrivately(Constants.Keys.Pref.USER, input)
    }

    val user: User?
        get() = getPrivately(Constants.Keys.Pref.USER, User::class.java, null)
}