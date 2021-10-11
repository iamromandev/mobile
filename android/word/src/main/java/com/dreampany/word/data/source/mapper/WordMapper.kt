package com.dreampany.word.data.source.mapper

import com.dreampany.common.misc.func.GsonParser
import com.dreampany.word.data.model.Language
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.remote.model.LanguageObject
import com.dreampany.word.data.source.remote.model.WordObject
import com.google.common.collect.Maps
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class WordMapper
@Inject constructor(
    private val parser: GsonParser
) {

    private val words: MutableMap<String, Word>
    private val languages: MutableMap<String, Language>

    init {
        words = Maps.newConcurrentMap()
        languages = Maps.newConcurrentMap()
    }

    @Synchronized
    fun convert(input: WordObject): Word {
        var word = words.get(input.id)
        if (word == null) {
            word = Word(input.id)
            words.put(input.id, word)
        }
        word.word = input.word
        word.origin = input.origin

        val language = convert(input.language)
        word.languageId = language.id
        word.language = language

        return word
    }

    @Synchronized
    fun convert(input: LanguageObject): Language {
        var language = languages.get(input.id)
        if (language == null) {
            language = Language(input.id)
            languages.put(input.id, language)
        }
        language.code = input.code
        language.name = input.name
        return language
    }


}