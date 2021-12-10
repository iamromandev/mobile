package com.dreampany.dictionary.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.misc.exts.dimension
import com.dreampany.common.ui.misc.ItemSpaceDecoration
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.PronuncationItemBinding
import com.dreampany.dictionary.ui.model.PronunciationItem
import com.dreampany.dictionary.ui.model.WordPartItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener

/**
 * Created by roman on 10/19/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class WordPartAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: WordPartItem<*, *>) -> Unit)? = null
) {

    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    val itemCount: Int get() = fastAdapter.adapterItems.size
    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView,
        reverse: Boolean = false
    ) {
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)

        recycler.apply {
            layoutManager = if (reverse) LinearLayoutManager(context).apply {
                stackFromEnd = true
            } else LinearLayoutManager(context)
            adapter = fastAdapter
            addItemDecoration(
                ItemSpaceDecoration(
                    context.dimension(R.dimen.recycler_horizontal_spacing).toInt(),
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    1,
                    true
                )
            )
        }

        fastAdapter.withSavedInstanceState(state)

        clickListener?.let { listener ->
            fastAdapter.addClickListener<PronuncationItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.speak) }
            )
            { view, position, adapter, item ->
                if (item is PronunciationItem) {
                    listener(view, item)
                }
            }
        }
    }

    fun addItem(recycler: RecyclerView, item: WordPartItem<*, *>) {
        val updated = updateItem(item)
        if (!updated) fastAdapter.add(item)
    }

    fun updateItem(item: WordPartItem<*, *>): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
        }
        return false
    }

    fun addItems(recycler: RecyclerView, items: List<WordPartItem<*, *>>?) {
        val scrollToFirst = isEmpty
        items?.forEach { item ->
            val updated = updateItem(item)
            if (!updated) fastAdapter.add(item)
        }
    }
}