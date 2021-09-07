package com.dreampany.tools.ui.home.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.misc.exts.addDecoration
import com.dreampany.framework.misc.exts.integer
import com.dreampany.tools.R
import com.dreampany.tools.databinding.FeatureItemBinding
import com.dreampany.tools.ui.home.model.FeatureItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import timber.log.Timber

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastFeatureAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, input: FeatureItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView
    ) {
        itemAdapter = ItemAdapter.items()
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is FeatureItem) true
            //item.item.name.toString().contains(constraint.toString(), ignoreCase = true)
            else
                false
        }
        footerAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        fastAdapter.addAdapter(1, footerAdapter)

        recycler.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = fastAdapter
            addDecoration(context.integer(R.integer.recycler_item_offset_small))
            //addItemDecoration(SpacingItemDecoration(3, context.dimension(R.dimen.recycler_vertical_spacing).toInt(), true))
            /*addItemDecoration(
                ItemSpaceDecoration(
                    context.dimension(R.dimen.recycler_horizontal_spacing).toInt(),
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    3,
                    true
                )
            )*/

            scrollListener?.let {
                scroller = object : EndlessRecyclerOnScrollListener(footerAdapter) {
                    override fun onLoadMore(currentPage: Int) {
                        it(currentPage)
                    }
                }
                addOnScrollListener(scroller)
            }
        }
        fastAdapter.withSavedInstanceState(state)

        clickListener?.let { listener ->
            /*fastAdapter.onClickListener = { view, adapter, item, position ->
                if (item is CoinItem)
                    view?.let {
                        listener(it, item)
                    }
                false
            }*/
            fastAdapter.addClickListener<FeatureItemBinding, GenericItem>(
                { bind -> bind.layout }, { bind -> arrayListOf(bind.full) }
            )
            { view, position, adapter, item ->
                if (item is FeatureItem) {
                    listener(view, item)
                }
            }
            /*fastAdapter.addClickListener<VideoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is VideoItem) {
                    listener(view, item)
                }
            }*/
        }

        fastAdapter.addEventHook(object : ClickEventHook<FeatureItem>() {
            /* override fun onBind(holder: RecyclerView.ViewHolder): View? {
                 if (holder is CoinItemBinding.)

                     return null
             }*/

            override fun onClick(
                view: View,
                position: Int,
                fastAdapter: FastAdapter<FeatureItem>,
                item: FeatureItem
            ) {
                Timber.v("View %s", view.toString())
            }

        })
    }

    fun destroy() {
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter.saveInstanceState(outState) ?: outState
    }

    fun filter(constraint: CharSequence?) {
        fastAdapter.filter(constraint)
    }

    fun showScrollProgress() {
        footerAdapter.clear()
        val progressItem = ProgressItem()
        progressItem.isEnabled = false
        footerAdapter.add(progressItem)
    }

    fun hideScrollProgress() {
        footerAdapter.clear()
    }

    fun addItems(items: List<FeatureItem>) {
        fastAdapter.add(items)
    }

    val itemCount: Long
        get() = fastAdapter.itemCount.toLong()

    val isEmpty: Boolean
        get() = itemCount == 0L

}