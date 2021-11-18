package com.dreampany.dictionary.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.*
import com.dreampany.common.misc.func.SimpleTextWatcher
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.dictionary.R
import com.dreampany.dictionary.data.enums.Action
import com.dreampany.dictionary.data.enums.State
import com.dreampany.dictionary.data.enums.Subtype
import com.dreampany.dictionary.data.enums.Type
import com.dreampany.dictionary.databinding.WordFragmentBinding
import com.dreampany.dictionary.ui.adapter.SourcePageAdapter
import com.dreampany.dictionary.ui.model.SourcePageItem
import com.dreampany.dictionary.ui.model.WordItem
import com.dreampany.dictionary.ui.vm.WordViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import android.view.Gravity

import com.google.android.material.tabs.TabLayout





/**
 * Created by roman on 10/15/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class WordFragment
@Inject constructor(

) : BaseFragment<WordFragmentBinding>() {

    val args: WordFragmentArgs by navArgs()

    override val layoutRes: Int = R.layout.word_fragment

    @Transient
    private var inited = false

    private val vm: WordViewModel by viewModels()
    private lateinit var pageAdapter: SourcePageAdapter

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
    }

    private fun initUi(state: Bundle?): Boolean {
        //if (inited) return true
        initPager()

        if (args.query.isNullOrEmpty()) {
            ex.postToUi(kotlinx.coroutines.Runnable {
                binding.query.requestFocus()
                showKeyboard()
            })
        } else {
            binding.query.setText(args.query)
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        vm.read("like")
        //requireActivity().getOnBackPressedDispatcher().addCallback(this, callback)
        binding.buttonBack.setOnSafeClickListener { onBackPressed() }

        binding.query.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                onClickOnQuery()
                true
            }
            false
        }

        binding.query.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                return@OnEditorActionListener true
            }
            false
        })

        binding.query.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(text: Editable) {
                updateQueryUi(text.toString())
            }
        })

        binding.clear.setOnSafeClickListener {
            clear()
        }

        binding.search.setOnSafeClickListener {
            search()
        }

        vm.subscribe(this, { this.processResponse(it) })
        return true
    }

    private fun initPager() {
        if (::pageAdapter.isInitialized) return
        pageAdapter = SourcePageAdapter(this)
        binding.pager.adapter = pageAdapter
        TabLayoutMediator(
            binding.tabs,
            binding.pager,
            { tab, position ->
                tab.text = pageAdapter.getTitle(position)
            }).attach()
    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }

    private fun onClickOnQuery() {
        ex.postToUi(kotlinx.coroutines.Runnable {
            binding.layoutSearch.setHeight(R.dimen.top_app_bar_height)
            binding.query.setTextSize(R.dimen.text_headline)
            binding.query.text = binding.query.text
            binding.query.setSelection(binding.query.length())
        })
    }

    private fun updateQueryUi(text: String) {
        if (text.isEmpty()) {
            binding.search.hide()
            binding.clear.hide()
        } else {
            binding.search.show()
            binding.clear.show()
        }
    }

    private fun clear() {
        binding.query.setText(null)
    }

    private fun search() {
        val query = binding.query.trimValue
        if (query.isEmpty()) {
            //TODO error handling
            return
        }
        vm.read(query)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, WordItem>) {
        if (response is Response.Progress) {
            if (response.progress) {
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
        ex.postToUi(kotlinx.coroutines.Runnable {
            binding.search.hide()
            binding.layoutSearch.setHeight(R.dimen.top_search_bar_height_collapsed)
            binding.query.setTextSize(R.dimen.text_headline_collapsed)
            binding.query.setFocusable(false)
            binding.query.setFocusableInTouchMode(false)
            binding.query.setFocusable(true)
            binding.query.setFocusableInTouchMode(true)
        })

        if (result != null) {
            if (!pageAdapter.isEmpty) pageAdapter.clear()
            pageAdapter.addItems(result.pages)

            for (i in 0 until binding.tabs.getTabCount()) {
                val tab: TabLayout.Tab = binding.tabs.getTabAt(i) ?: continue
                //tab.view.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            }
        }
    }

    private val WordItem.pages: List<SourcePageItem>
        get() {
            val pages = mutableListOf<SourcePageItem>()
            this.sources.sortedWith({ a, b -> a.source.compareTo(b.source, true) }).forEach {
                pages.add(SourcePageItem(it, this.input))
            }
            return pages
        }
}