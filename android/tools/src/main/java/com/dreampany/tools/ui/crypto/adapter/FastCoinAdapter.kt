package com.dreampany.tools.ui.crypto.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.adapter.SpacingItemDecoration
 import com.dreampany.framework.misc.exts.dimension
import com.dreampany.tools.R
 import com.dreampany.tools.data.model.crypto.Currency
import com.dreampany.tools.databinding.CoinInfoItemBinding
import com.dreampany.tools.databinding.CoinItemBinding
import com.dreampany.tools.ui.crypto.model.CoinItem
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericFastItemAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import com.mikepenz.fastadapter.utils.ComparableItemListImpl

/**
 * Created by roman on 13/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class FastCoinAdapter(
    val scrollListener: ((currentPage: Int) -> Unit)? = null,
    val clickListener: ((view: View, item: CoinItem) -> Unit)? = null
) {

    private lateinit var scroller: EndlessRecyclerOnScrollListener
    private lateinit var fastAdapter: GenericFastItemAdapter
    private lateinit var itemAdapter: GenericItemAdapter
    private lateinit var footerAdapter: GenericItemAdapter

    private lateinit var capComparator: Comparator<GenericItem>
    private val rankComparator: Comparator<GenericItem>

    init {
        rankComparator = RankComparator()
    }

    val itemCount: Int
        get() = fastAdapter.adapterItems.size

    val isEmpty: Boolean get() = itemCount == 0

    fun initRecycler(
        state: Bundle?,
        recycler: RecyclerView,
        currency: Currency,
        sort: String,
        order: String
    ) {
        capComparator = CryptoComparator(recycler.context, currency, sort, order)
        val list = ComparableItemListImpl(comparator = capComparator)
        itemAdapter = ItemAdapter(list)
        itemAdapter.itemFilter.filterPredicate = { item: GenericItem, constraint: CharSequence? ->
            if (item is CoinItem)
                item.input.first.name.contains(constraint.toString(), ignoreCase = true)
            else
                false
        }
        footerAdapter = ItemAdapter.items()
        fastAdapter = FastItemAdapter(itemAdapter)
        fastAdapter.addAdapter(1, footerAdapter)

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
            addItemDecoration(
                SpacingItemDecoration(
                    1,
                    context.dimension(R.dimen.recycler_vertical_spacing).toInt(),
                    true
                )
            )

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
            fastAdapter.addClickListener<CoinItemBinding, GenericItem>(
                { bind -> bind.root }, { bind -> arrayListOf(bind.layoutOptions.buttonFavorite) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }

            fastAdapter.addClickListener<CoinInfoItemBinding, GenericItem>(
                { bind -> bind.root },
                { bind -> arrayListOf(bind.root) }
            )
            { view, position, adapter, item ->
                if (item is CoinItem) {
                    listener(view, item)
                }
            }
        }
    }

    fun destroy() {
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

    fun updateItem(item: CoinItem): Boolean {
        var position = fastAdapter.getAdapterPosition(item)
        position = fastAdapter.getGlobalPosition(position)
        if (position >= 0) {
            fastAdapter.set(position, item)
            return true
            //fastAdapter.notifyAdapterItemChanged(position)
        }
        return false
    }

    fun updateItems(items: List<CoinItem>) {
        items.forEach {
            updateItem(it)
        }
    }

    fun addItem(item: CoinItem) {
        val updated = updateItem(item)
        if (!updated)
            fastAdapter.add(item)
    }

    fun addItems(items: List<CoinItem>) {
        fastAdapter.add(items)
    }

    class CryptoComparator(
        private val context: Context,
        private val currency: Currency,
        private val sort: String,
        private val order: String
    ) : Comparator<GenericItem> {
        val String.isMarketCap: Boolean
            get() = this == context.getString(R.string.key_crypto_settings_sort_value_market_cap)

        val String.isDescending: Boolean
            get() = this == context.getString(R.string.key_crypto_settings_order_value_descending)

        override fun compare(left: GenericItem, right: GenericItem): Int {
            if (left is CoinItem && right is CoinItem) {
                if (sort.isMarketCap) {
                    val leftCap = left.input.second
                    val rightCap = right.input.second
                    if (order.isDescending) {
                        return (rightCap.getMarketCap() - leftCap.getMarketCap()).toInt()
                    } else {
                        return (leftCap.getMarketCap() - rightCap.getMarketCap()).toInt()
                    }
                }
            }
            return 0
        }
    }

    class RankComparator : Comparator<GenericItem> {
        override fun compare(left: GenericItem, right: GenericItem): Int {
           /* if (left is CoinItem && right is CoinItem) {
                return left.input.rank - right.input.rank
            }*/
            return 0
        }
    }
}