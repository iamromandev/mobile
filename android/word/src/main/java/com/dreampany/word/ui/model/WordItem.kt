package com.dreampany.word.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dreampany.word.R
import com.dreampany.word.data.model.Word
import com.dreampany.word.databinding.WordItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class WordItem<B>(val input: Word) :
    ModelAbstractBindingItem<Word, WordItemBinding>(input) {

    override fun hashCode(): Int = input.hashCode()

    override fun equals(other: Any?): Boolean = input.equals(other)

    override var identifier: Long = hashCode().toLong()

    override val type: Int
        get() = R.id.adapter_word_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): WordItemBinding = WordItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: WordItemBinding, payloads: List<Any>) {
    }
}