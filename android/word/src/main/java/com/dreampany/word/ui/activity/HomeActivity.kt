package com.dreampany.word.ui.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.word.R
import com.dreampany.word.databinding.HomeActivityBinding
import com.dreampany.word.ui.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 10/1/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeActivityBinding>() {

    override val layoutRes: Int = R.layout.home_activity
    override val toolbarId: Int = R.id.toolbar

    @Transient
    private var inited = false

    override fun onStartUi(state: Bundle?) {
        inited = initUi()
    }

    override fun onStopUi() {

    }

    override fun onBackPressed() {
        val currentFragment = currentFragmentOfNavHost
        if (currentFragment is HomeFragment) {
            if (currentFragment.hasBackPressed) return
        }

        super.onBackPressed()
    }

    private fun initUi(): Boolean {
        if (inited) return true
        val navView: BottomNavigationView = binding.navView

        val controller = findNavController(R.id.nav_host)
        val configuration = AppBarConfiguration(
            setOf(
                R.id.navigation_home
            )
        )
        setupActionBarWithNavController(controller, configuration)
        navView.setupWithNavController(controller)

        return true
    }
}