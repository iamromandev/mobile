package com.dreampany.hi.ui.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.hi.R
import com.dreampany.hi.databinding.HomeActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

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

    private fun initUi(): Boolean {
        if (inited) return true

        val navView: BottomNavigationView = binding.navView

        val controller = findNavController(R.id.nav_host)
        val configuration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_chat,
                R.id.navigation_download,
                R.id.navigation_notification,
                R.id.navigation_more
            )
        )
        setupActionBarWithNavController(controller, configuration)
        navView.setupWithNavController(controller)

        return true
    }
}