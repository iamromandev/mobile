package com.dreampany.word.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.init
import com.dreampany.common.misc.exts.open
import com.dreampany.common.misc.exts.randomId
import com.dreampany.common.misc.exts.refresh
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.ui.misc.StatefulLayout
import com.dreampany.common.ui.model.UiTask
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

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
        //vm.unregisterNearby()
    }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true

        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            //vm.registerNearby()
        }



        binding.swipe.init(this)
        //binding.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)

        return true
    }


}