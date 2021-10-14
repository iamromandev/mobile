package com.dreampany.dictionary.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.MotionEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.hideKeyboard
import com.dreampany.common.misc.exts.setOnSafeClickListener
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

    private val speechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        ?.firstOrNull() ?: return@registerForActivityResult
                vm.read(spokenText)
            }
        }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true
        binding.editEnter.requestFocus()
        binding.editEnter.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_DOWN == event.action) {
                openOcrUi()
            }
            true
        }

        binding.speak.setOnSafeClickListener {
            displaySpeechRecognizer()
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

    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        speechLauncher.launch(intent)
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