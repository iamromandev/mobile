package com.dreampany.dictionary.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.common.misc.exts.html
import com.dreampany.dictionary.R
import com.dreampany.dictionary.databinding.SourceDefinitionsItemBinding

/**
 * Created by roman on 10/19/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class SourceDefinitionsItem
constructor(
    override var input: String
) : WordPartItem<String, SourceDefinitionsItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()
    override fun equals(other: Any?): Boolean = input.equals(other)
    override var identifier: Long = hashCode().toLong()
    override val type: Int
        get() = R.id.adapter_source_definitions_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SourceDefinitionsItemBinding = SourceDefinitionsItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: SourceDefinitionsItemBinding, payloads: List<Any>) {
        binding.definitions.text = input.html
    }
}