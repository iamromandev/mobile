package com.dreampany.hi.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.common.misc.exts.format
import com.dreampany.hi.R
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.databinding.OutTextMessageItemBinding
import com.dreampany.hi.misc.constant.Constants

/**
 * Created by roman on 8/26/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class OutTextMessageItem(
   override var input: Message
) : MessageItem<OutTextMessageItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_out_text_message_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): OutTextMessageItemBinding = OutTextMessageItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: OutTextMessageItemBinding, payloads: List<Any>) {
        binding.body.text = input.text
        binding.datetime.text = input.createdAt.format(Constants.Datetime.MESSAGE_PATTERN)
    }
}