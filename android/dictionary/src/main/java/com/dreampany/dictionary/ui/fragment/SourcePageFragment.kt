package com.dreampany.dictionary.ui.fragment

import android.os.Bundle
import com.dreampany.common.misc.exts.task
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.ui.model.UiTask
import com.dreampany.dictionary.R
import com.dreampany.dictionary.data.enums.Action
import com.dreampany.dictionary.data.enums.State
import com.dreampany.dictionary.data.enums.Subtype
import com.dreampany.dictionary.data.enums.Type
import com.dreampany.dictionary.data.model.Definition
import com.dreampany.dictionary.data.model.Word
import com.dreampany.dictionary.databinding.SourcePageFragmentBinding
import com.dreampany.dictionary.ui.adapter.WordPartAdapter
import com.dreampany.dictionary.ui.model.PronunciationItem
import com.dreampany.dictionary.ui.model.DefinitionsItem
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 10/17/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class SourcePageFragment
@Inject constructor(

) : BaseFragment<SourcePageFragmentBinding>() {

    override val layoutRes: Int = R.layout.source_page_fragment

    @Transient
    private var inited = false

    private lateinit var adapter: WordPartAdapter
    private lateinit var word: Word
    private lateinit var source: String

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Word>
        word = task.input ?: return
        source = task.extra ?: return
        Timber.v("Source : $source")
        inited = initUi(state)
    }

    override fun onStopUi() {
    }

    private fun initUi(state: Bundle?): Boolean {
        //if (inited) return true

        adapter = WordPartAdapter()
        adapter.initRecycler(state, binding.recycler)

        val pronunciation = word.findPronunciation(source)
        if (pronunciation != null) {
            adapter.addItem(binding.recycler, PronunciationItem(pronunciation))
        }

        val definitions = word.findDefinitions(source)
        if (definitions.isNotEmpty()) {
            adapter.addItem(binding.recycler, DefinitionsItem(definitions.toString))
        }

        return true
    }

    private val List<Definition>.toString: String
        get() =
            this.map { "${it.partOfSpeech.partOfSpeech} . ${it.definition}" }
                .joinToString(separator = "<br/><br/>")

}