package com.dreampany.dictionary.ui.adapter

import androidx.fragment.app.Fragment
import com.dreampany.common.ui.adapter.BasePagerAdapter
import com.dreampany.common.ui.model.UiTask
import com.dreampany.dictionary.data.enums.Action
import com.dreampany.dictionary.data.enums.State
import com.dreampany.dictionary.data.enums.Subtype
import com.dreampany.dictionary.data.enums.Type
import com.dreampany.dictionary.ui.fragment.SourcePageFragment
import com.dreampany.dictionary.ui.model.SourcePageItem

/**
 * Created by roman on 10/17/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class SourcePageAdapter (fragment: Fragment) : BasePagerAdapter<Fragment>(fragment) {
    private val pages = arrayListOf<SourcePageItem>()

    fun addItems(items: List<SourcePageItem>) {
        pages.clear()

        items.forEach {
            pages.add(it)
            val task = UiTask(
                Type.USER,
                Subtype.DEFAULT,
                State.DEFAULT,
                Action.DEFAULT,
                it.word
            )
            addItem(
                com.dreampany.common.misc.exts.createFragment(
                    SourcePageFragment::class,
                    task
                ),
                it.source.source,
                false
            )
        }
        notifyDataSetChanged()
    }

}