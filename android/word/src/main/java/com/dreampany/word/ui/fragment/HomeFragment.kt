package com.dreampany.word.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.word.R
import com.dreampany.word.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 10/1/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class HomeFragment @Inject constructor() : BaseFragment<HomeFragmentBinding>() {

    @Transient
    private var inited = false

    override val layoutRes: Int = R.layout.home_fragment
    override val menuRes: Int = R.menu.home_menu
    override val searchMenuItemId: Int = R.id.action_search

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
        //vm.unregisterNearby()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_search -> {
                // Open the search view on the menu item click.
                //searchView.openSearch()
                //binding.searchView.openSearch()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //if (query.isNullOrEmpty()) return false

        Timber.v(query)

        return super.onQueryTextSubmit(query)
    }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true

        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            //vm.registerNearby()
        }

        //binding.swipe.init(this)
        //binding.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)

        return true
    }




}