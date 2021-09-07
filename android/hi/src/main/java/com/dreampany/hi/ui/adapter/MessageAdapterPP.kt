package com.dreampany.hi.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dreampany.common.misc.exts.inflater
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.databinding.InTextMessageItemBinding
import com.dreampany.hi.databinding.OutTextMessageItemBinding
import com.dreampany.hi.ui.adapter.vh.InTextMessageViewHolder
import com.dreampany.hi.ui.adapter.vh.MessageViewHolder
import com.dreampany.hi.ui.adapter.vh.OutTextMessageViewHolder

/**
 * Created by roman on 8/24/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class MessageAdapterPP(val author: String) :
    PagingDataAdapter<Message, MessageViewHolder>(COMPARATOR) {
    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem

        }
    }

    enum class ViewType(val value: Int) {
        OUT_TEXT(1), IN_TEXT(2);
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position) ?: return 0
        val out = message.author == author
        when (message.type) {
            Message.Type.TEXT -> {
                return if (out) ViewType.OUT_TEXT.value else ViewType.IN_TEXT.value
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ViewType.OUT_TEXT.value ->
                OutTextMessageViewHolder(
                    OutTextMessageItemBinding.inflate(
                        parent.inflater,
                        parent,
                        false
                    )
                )
            ViewType.IN_TEXT.value ->
                InTextMessageViewHolder(
                    InTextMessageItemBinding.inflate(
                        parent.inflater,
                        parent,
                        false
                    )
                )
            else -> OutTextMessageViewHolder(
                OutTextMessageItemBinding.inflate(
                    parent.inflater,
                    parent,
                    false
                )
            )
        }

    }
}