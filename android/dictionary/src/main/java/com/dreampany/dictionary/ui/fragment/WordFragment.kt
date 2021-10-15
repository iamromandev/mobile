package com.dreampany.dictionary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.WordFragmentBinding
import com.dreampany.dictionary.databinding.WordItemBinding
import com.dreampany.dictionary.ui.vm.WordViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    override val layoutRes: Int = R.layout.word_fragment

    @Transient
    private var inited = false

    private val vm: WordViewModel by viewModels()

    override fun onStartUi(state: Bundle?) {
        inited = initUi(state)
    }

    override fun onStopUi() {
    }

    private fun initUi(state: Bundle?): Boolean {
        if (inited) return true

        return true
    }
}