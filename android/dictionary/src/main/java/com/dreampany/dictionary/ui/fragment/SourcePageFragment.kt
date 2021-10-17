package com.dreampany.dictionary.ui.fragment

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.SourcePageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 10/17/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SourcePageFragment
@Inject constructor(

) : BaseFragment<SourcePageFragmentBinding>() {

    override val layoutRes: Int = R.layout.source_page_fragment

    @Transient
    private var inited = false

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }

}