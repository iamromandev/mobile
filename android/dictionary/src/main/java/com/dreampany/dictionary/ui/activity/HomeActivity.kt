package com.dreampany.dictionary.ui.activity

import android.os.Bundle
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.HomeActivityBinding
import com.dreampany.dictionary.ui.fragment.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 10/1/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeActivityBinding>() {

    @Inject
    internal lateinit var homeFragment: HomeFragment

    override val layoutRes: Int = R.layout.home_activity

    @Transient
    private var inited = false

    override fun onStartUi(state: Bundle?) {
        inited = initUi()
    }

    override fun onStopUi() {

    }

    override fun onBackPressed() {
/*        val currentFragment = currentFragmentOfNavHost
        if (currentFragment is HomeFragment) {
            if (currentFragment.hasBackPressed) return
        }*/

        super.onBackPressed()
    }

    private fun initUi(): Boolean {
        if (inited) return true

        supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment, homeFragment)
            .commit()

        return true
    }
}