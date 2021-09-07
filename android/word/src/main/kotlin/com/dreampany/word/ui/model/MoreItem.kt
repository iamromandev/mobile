package com.dreampany.word.ui.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.data.model.BaseKt
import com.dreampany.framework.ui.model.BaseItem
import com.dreampany.framework.util.TextUtil
import com.dreampany.word.R
import com.dreampany.word.data.model.More
import com.dreampany.word.ui.adapter.MoreAdapter
import com.dreampany.word.ui.enums.MoreType
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MoreItem private constructor(item: More, @LayoutRes layoutId: Int = 0) :
    BaseItem<More, MoreItem.ViewHolder, String>(item, layoutId) {

    companion object {
        fun getItem(item: More): MoreItem {
            return MoreItem(item, R.layout.item_more)
        }
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ViewHolder(view, adapter)
    }

    override fun filter(constraint: String): Boolean {
        return false
    }

    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseItem.ViewHolder(view, adapter) {

        private var adapter: MoreAdapter
        private var icon: ImageView
        private var title: TextView

        init {
            this.adapter = adapter as MoreAdapter
            icon = view.findViewById(R.id.icon)
            title = view.findViewById(R.id.title)
        }

        override fun <VH : BaseItem.ViewHolder, T : BaseKt, S : Serializable, I : BaseItem<T, VH, S>>
                bind(position: Int, item: I) {
            val moreItem = item as MoreItem
            val more = moreItem.item
            when (more.type) {
                MoreType.APPS -> {
                    icon.setImageResource(R.drawable.ic_apps_black_24dp)
                    title.setText(
                        TextUtil.getString(
                            getContext(),
                            R.string.more_apps
                        )
                    )
                }
                MoreType.RATE_US -> {
                    icon.setImageResource(R.drawable.ic_rate_review_black_24dp)
                    title.setText(TextUtil.getString(getContext(), R.string.rate_us))
                }
                MoreType.FEEDBACK -> {
                    icon.setImageResource(R.drawable.ic_feedback_black_24dp)
                    title.setText(
                        TextUtil.getString(
                            getContext(),
                            R.string.title_feedback
                        )
                    )
                }
                MoreType.SETTINGS -> {
                    icon.setImageResource(R.drawable.ic_settings_black_24dp)
                    title.setText(TextUtil.getString(getContext(), R.string.settings))
                }
                MoreType.LICENSE -> {
                    icon.setImageResource(R.drawable.ic_security_black_24dp)
                    title.setText(TextUtil.getString(getContext(), R.string.license))
                }
                MoreType.ABOUT -> {
                    icon.setImageResource(R.drawable.ic_info_black_24dp)
                    title.setText(TextUtil.getString(getContext(), R.string.about))
                }
            }
        }
    }
}