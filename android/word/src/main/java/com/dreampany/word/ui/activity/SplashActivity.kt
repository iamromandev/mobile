package com.dreampany.word.ui.activity

import android.os.Bundle
import com.dreampany.common.misc.exts.open
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.word.R
import com.dreampany.word.data.source.pref.Pref
import com.dreampany.word.databinding.SplashActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 9/30/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashActivityBinding>() {

    @Inject
    internal lateinit var pref: Pref

    override val layoutRes: Int = R.layout.splash_activity

    override fun onStartUi(state: Bundle?) {
        ex.postToUi { nextUi() }
    }

    override fun onStopUi() {
    }

    private fun nextUi() {
        if (pref.isStarted || pref.isLogged) {
            openHomeUi()
        } else if (pref.isSigned) {
            openAuthInfoUi()
        } else {
            //open(AuthActivity::class, true
            openHomeUi()
        }
    }

    private fun openHomeUi() {
        open(HomeActivity::class, true)
    }

    private fun openAuthInfoUi() {
        /* val auth = pref.auth ?: return
         val task = UiTask(
             Type.AUTH,
             Subtype.DEFAULT,
             State.DEFAULT,
             Action.DEFAULT,
             auth
         )
         open(AuthInfoActivity::class, task, true)*/
    }


}