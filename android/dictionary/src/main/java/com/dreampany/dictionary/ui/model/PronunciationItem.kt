package com.dreampany.dictionary.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.dictionary.R
import com.dreampany.dictionary.data.model.Pronunciation
import com.dreampany.dictionary.databinding.PronuncationItemBinding

/**
 * Created by roman on 10/19/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class PronunciationItem
constructor(
    override var input: Pronunciation
) : WordPartItem<Pronunciation, PronuncationItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()
    override fun equals(other: Any?): Boolean = input.equals(other)
    override var identifier: Long = hashCode().toLong()
    override val type: Int
        get() = R.id.adapter_pronunciation_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): PronuncationItemBinding = PronuncationItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: PronuncationItemBinding, payloads: List<Any>) {
        binding.pronunciation.text = input.pronunciation
    }
}