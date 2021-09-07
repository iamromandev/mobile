package com.dreampany.hi.ui.fragment

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseSheetFragment
import com.dreampany.hi.R
import com.dreampany.hi.databinding.FileSheetFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by roman on 9/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class FileSheetFragment @Inject constructor() : BaseSheetFragment<FileSheetFragmentBinding>() {

    @Transient
    private var inited = false

    override val layoutRes: Int = R.layout.file_sheet_fragment

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