package com.dreampany.hello.ui.auth.activity

import android.os.Bundle
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R
import com.dreampany.hello.data.source.pref.Pref
import com.dreampany.hello.databinding.AuthActivityBinding
import com.dreampany.hello.ui.home.activity.HomeActivity
import javax.inject.Inject

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthActivity : InjectActivity() {

    @Inject
    internal lateinit var pref: Pref

    private lateinit var bind: AuthActivityBinding

    override val layoutRes: Int = R.layout.auth_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()

        bind.login.setOnSafeClickListener {
            open(LoginActivity::class)
        }

        bind.signup.setOnSafeClickListener {
            open(SignupActivity::class)
        }

        bind.start.setOnClickListener {
            pref.start()
            open(HomeActivity::class, true)
        }
    }
}