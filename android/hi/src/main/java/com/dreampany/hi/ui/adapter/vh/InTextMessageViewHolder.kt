package com.dreampany.hi.ui.adapter.vh

import com.dreampany.common.misc.exts.format
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.databinding.InTextMessageItemBinding
import com.dreampany.hi.misc.constant.Constants

/**
 * Created by roman on 8/24/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class InTextMessageViewHolder(val binding: InTextMessageItemBinding) :
    MessageViewHolder(binding) {

    override fun bind(input: Message) {
        binding.body.text = input.text
        binding.datetime.text = input.createdAt.format(Constants.Datetime.MESSAGE_PATTERN)
    }
}