package com.dreampany.hi.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.common.misc.exts.dimension
import com.dreampany.common.ui.misc.ItemSpaceDecoration
import com.dreampany.hi.R
import com.dreampany.hi.databinding.UserItemBinding
import com.dreampany.hi.ui.model.MessageItem
import com.dreampany.hi.ui.model.UserItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnTopScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import timber.log.Timber

/**
 * Created by roman on 8/26/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class MessageAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: MessageItem<*>) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnTopScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    private val timeComparator: Comparator<GenericItem>

    init {
        timeComparator = TimeComparator()
    }

    val itemCount: Int get() = fastAdapter.adapterItems.size
    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView,
        reverse: Boolean = false
    ) {
        val list = ComparableItemListImpl(comparator = timeComparator)
        itemAdapter = ItemAdapter(list)
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is UserItem)
                item.input.name.toString().contains(constraint.toString(), ignoreCase = true)
            else
                false
        }
        footerAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        fastAdapter.addAdapter(1, footerAdapter)

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

            scrollListener?.let {
                scroller = object : EndlessRecyclerOnTopScrollListener(fastAdapter) {
                    override fun onLoadMore(page: Int) {
                        Timber.v("MessageAdapter onLoadMore")
                        it(page)
                    }

                    override fun onNothingToLoad() {
                         Timber.v("MessageAdapter onNothingToLoad")
                    }
                }
                //scroller.disable()
                addOnScrollListener(scroller)
            }
/*            scrollListener?.let {
                val listener = object : OnVerticalScrollListener() {
                    override fun onScrolledToTop() {
                        it(0)
                    }
                }
                addOnScrollListener(listener)
            }*/
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
            fastAdapter.addClickListener<UserItemBinding, GenericItem>(
                { bind -> bind.root }
            )
            { view, position, adapter, item ->
                if (item is UserItem) {
                    //listener(view, item)
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

        fastAdapter.addEventHook(object : ClickEventHook<UserItem>() {
            /* override fun onBind(holder: RecyclerView.ViewHolder): View? {
                 if (holder is CoinItemBinding.)

                     return null
             }*/

            override fun onClick(
                view: View,
                position: Int,
                fastAdapter: FastAdapter<UserItem>,
                item: UserItem
            ) {
                Timber.v("View %s", view.toString())
            }

        })
    }

    fun destroy() {
    }

    fun clearAll() {
        fastAdapter.clear()
    }

    fun saveInstanceState(outState: Bundle): Bundle {
        return fastAdapter.saveInstanceState(outState)
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

    fun updateItem(item: MessageItem<*>): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<MessageItem<*>>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(recycler: RecyclerView, item: MessageItem<*>) {
        val updated = updateItem(item)
        if (!updated) fastAdapter.add(item)
        recycler.smoothScrollToPosition(itemCount - 1)
    }

    fun addItems(recycler: RecyclerView, items: List<MessageItem<*>>?) {
        val scrollToFirst = isEmpty
        items?.forEach { item ->
            val updated = updateItem(item)
            if (!updated) fastAdapter.add(item)
        }
        if (scrollToFirst) recycler.smoothScrollToPosition(itemCount - 1)
        //if (scrollToFirst) scroller.enable()
        //scroller.completeLoading()
    }

    class TimeComparator(
    ) : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is MessageItem<*> && right is MessageItem<*>) {
                return (left.input.createdAt - right.input.createdAt).toInt()
            }
            return 0
        }
    }
}