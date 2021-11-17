package com.dreampany.dictionary.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.common.misc.exts.html
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.DefinitionsItemBinding

/**
 * Created by roman on 10/19/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class DefinitionsItem
constructor(
    override var input: String
) : WordPartItem<String, DefinitionsItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()
    override fun equals(other: Any?): Boolean = input.equals(other)
    override var identifier: Long = hashCode().toLong()
    override val type: Int
        get() = R.id.adapter_source_definitions_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DefinitionsItemBinding = DefinitionsItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: DefinitionsItemBinding, payloads: List<Any>) {
        binding.definitions.text = input.html
    }
}