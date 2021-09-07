package com.dreampany.hi.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.hi.R
import com.dreampany.hi.data.model.User
import com.dreampany.hi.databinding.UserItemBinding
import com.dreampany.hi.misc.exts.setRes
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 7/17/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class UserItem(
    var input: User,
    var nearby: Boolean = false,
    var internet: Boolean = false
) : ModelAbstractBindingItem<User, UserItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_user_item_id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): UserItemBinding =
        UserItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: UserItemBinding, payloads: List<Any>) {
        //binding.icon.setUrl(input.favicon)
        val statusRes =
            if (nearby) R.drawable.ic_status_live_24 else R.drawable.ic_status_dead_24
        binding.nearbyStatus.setRes(statusRes)

        binding.title.text = input.name
    }

}