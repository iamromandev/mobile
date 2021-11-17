package com.dreampany.dictionary.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dreampany.dictionary.R
import com.dreampany.dictionary.data.model.Source
import com.dreampany.dictionary.data.model.Word
import com.dreampany.dictionary.databinding.WordItemBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class WordItem(val input: Word) :
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

    val sources : List<Source>
        get() {
            val output = mutableSetOf<Source>()
            //input.pronunciations.forEach { output.add(it.source) }
            input.definitions.forEach { output.add(it.source) }
            return output.toMutableList()
        }

    val pronunciation: String?
        get() {
            return input.pronunciations.filter { it.pronunciation.isNotEmpty() }
                .minByOrNull { it.pronunciation.length }?.pronunciation
        }

    val definition: String?
        get() {
            val definition = input.definitions.filter { it.definition.isNotEmpty() }.firstOrNull() ?: return null
            return "${definition.partOfSpeech.partOfSpeech} . ${definition.definition}"
        }
}