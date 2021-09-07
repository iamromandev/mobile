package com.dreampany.tools.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.Action
import com.dreampany.tools.data.enums.State
import com.dreampany.tools.data.enums.Subtype
import com.dreampany.tools.data.enums.Type
import com.dreampany.tools.data.model.home.Feature
import com.dreampany.tools.databinding.HomeFragmentBinding
import com.dreampany.tools.ui.crypto.activity.CoinsActivity
import com.dreampany.tools.ui.history.activity.HistoriesActivity
import com.dreampany.tools.ui.home.adapter.FastFeatureAdapter
import com.dreampany.tools.ui.home.model.FeatureItem
import com.dreampany.tools.ui.home.vm.FeatureViewModel
import com.dreampany.tools.ui.news.activity.NewsActivity
import com.dreampany.tools.ui.note.activity.NotesActivity
import com.dreampany.tools.ui.radio.activity.RadioActivity
import com.dreampany.tools.ui.wifi.activity.WifisActivity
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: HomeFragmentBinding
    private lateinit var adapter: FastFeatureAdapter
    private lateinit var vm: FeatureViewModel

    override val layoutRes: Int = R.layout.home_fragment

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        if (adapter.isEmpty)
            vm.loadFeatures()
    }

    override fun onStopUi() {
    }

    private fun onItemPressed(view: View, input: FeatureItem) {
         when (view.id) {
            R.id.layout -> {
               openUi(input.input)
            }
            R.id.full -> {
                openFullUi(input.input)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(FeatureViewModel::class)

        vm.subscribes(this, Observer { this.processResponse(it) })

        bind.swipe.disable()
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastFeatureAdapter({ currentPage: Int ->
            Timber.v("CurrentPage: %d", currentPage)
            onRefresh()
        }, this::onItemPressed)

        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, List<FeatureItem>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<FeatureItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<FeatureItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun openUi(input: Feature) {
        when (input.subtype) {
            Subtype.WIFI -> activity.open(WifisActivity::class)
            Subtype.CRYPTO -> activity.open(CoinsActivity::class)
            Subtype.RADIO -> activity.open(RadioActivity::class)
            Subtype.NOTE -> activity.open(NotesActivity::class)
            Subtype.HISTORY -> activity.open(HistoriesActivity::class)
            Subtype.NEWS -> activity.open(NewsActivity::class)
        }
    }

    private fun openFullUi(input: Feature) {
        activity.rateUs(input.packageName)
    }
}