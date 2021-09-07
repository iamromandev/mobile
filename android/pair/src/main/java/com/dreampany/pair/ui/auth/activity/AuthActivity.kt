package com.dreampany.pair.ui.auth.activity

import android.os.Bundle
import android.view.View
import com.dreampany.common.misc.extension.open
import com.dreampany.common.misc.extension.setOnSafeClickListener
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.pair.R
import com.dreampany.pair.databinding.AuthActivityBinding

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AuthActivity : InjectActivity() {

    private lateinit var bind: AuthActivityBinding

    override fun fullScreen(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int = R.layout.auth_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun onSafeClick(view: View) {
        when (view) {
            bind.buttonRegister -> {
                registerPressed()
            }
            bind.buttonLogin -> {
                loginPressed()
            }
        }
    }

    private fun initUi() {
        bind = getBinding()
        bind.buttonRegister.setOnSafeClickListener(this::onSafeClick)
        bind.buttonLogin.setOnSafeClickListener(this::onSafeClick)
    }

    private fun registerPressed() {
        open(RegisterActivity::class)
    }

    private fun loginPressed() {
        open(LoginActivity::class)
    }
}