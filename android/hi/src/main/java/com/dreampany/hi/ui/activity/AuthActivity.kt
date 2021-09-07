package com.dreampany.hi.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.dreampany.common.misc.exts.open
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.hi.R
import com.dreampany.hi.databinding.AuthActivityBinding
import com.dreampany.hi.ui.activity.HomeActivity

import com.dreampany.hi.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 5/7/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class AuthActivity : BaseActivity<AuthActivityBinding>() {

    private val vm: UserViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.auth_activity

    override fun onStartUi(state: Bundle?) {
         initUi()
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        binding.login.setOnClickListener {
            //open(LoginActivity::class)
        }

        binding.register.setOnClickListener {
            //open(RegisterActivity::class)
        }

        binding.start.setOnClickListener {
            vm.createAnonymousUser()
            open(HomeActivity::class, finish = true)
        }
    }
}