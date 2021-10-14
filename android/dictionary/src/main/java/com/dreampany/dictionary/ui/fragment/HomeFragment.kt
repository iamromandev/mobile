package com.dreampany.dictionary.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.hide
import com.dreampany.common.misc.exts.hideKeyboard
import com.dreampany.common.misc.exts.setOnSafeClickListener
import com.dreampany.common.misc.exts.show
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.dictionary.R
import com.dreampany.dictionary.data.enums.Action
import com.dreampany.dictionary.data.enums.State
import com.dreampany.dictionary.data.enums.Subtype
import com.dreampany.dictionary.data.enums.Type
import com.dreampany.dictionary.databinding.HomeFragmentBinding
import com.dreampany.dictionary.ui.model.WordItem
import com.dreampany.dictionary.ui.vm.WordViewModel
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

    @Inject
    internal lateinit var ocrFragment: OcrFragment

    override val layoutRes: Int = R.layout.home_fragment
    override val menuRes: Int = R.menu.home_menu
    override val searchMenuItemId: Int = R.id.action_search

    @Transient
    private var inited = false

    private val vm: WordViewModel by viewModels()

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
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
        if (query.isNullOrEmpty()) return false

        Timber.v(query)

        vm.read(query)

        return super.onQueryTextSubmit(query)
    }

/*    override val hasBackPressed: Boolean
        get() {
            if (ocrFragment.isVisible) {
                val text = ocrFragment.texts
                Timber.v(text.toString())
                closeOcrSheet()
                return true
            }
            return false
        }*/

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true

        binding.fab.setOnSafeClickListener {
            openOcrUi()
        }

        vm.subscribe(this, { this.processResponse(it) })

        /*ocrSheetFragment.setListener({
            ex.postToUi({
                closeOcrSheet()
            })
        })*/

        /*runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            //vm.registerNearby()
        }*/

        //binding.swipe.init(this)
        //binding.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_stations)

        return true
    }

    private fun openOcrUi() {
        findNavController().navigate(R.id.action_home_to_ocr)
        //binding.fab.hide()

/*        binding.ocrSheetFragment.show()
        childFragmentManager.beginTransaction()
            .replace(R.id.ocr_sheet_fragment, ocrFragment)
            .commit()*/
    }

    private fun closeOcrSheet() {
        //childFragmentManager.beginTransaction().remove(ocrFragment).commit();
        //binding.fab.show()
        //binding.ocrSheetFragment.hide()
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, WordItem>) {
        if (response is Response.Progress) {
            //binding.swipe.refresh(response.progress)
            if (response.progress) {
                hideSearchView()
                hideKeyboard()
            }

            applyProgress(response.progress)

        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, WordItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: WordItem?) {
        if (result != null) {

        }

    }


}