package com.dreampany.dictionary.data.source.remote

import com.dreampany.dictionary.data.model.Word
import com.dreampany.dictionary.data.source.api.DictionaryDataSource
import com.dreampany.dictionary.data.source.mapper.DictionaryMapper
import com.dreampany.dictionary.data.source.remote.model.WordObject

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class DictionaryRemoteDataSource
constructor(
    private val dictionaryMapper: DictionaryMapper,
    private val dictionaryService: DictionaryService
) : DictionaryDataSource {

    override suspend fun read(word: String): Word? {
        val response = dictionaryService.getWord(word)
        if (!response.isSuccessful) return null

        val input: WordObject = response.body() ?: return null
        val output: Word = dictionaryMapper.createWord(input) ?: return null
        if (input.pronunciations != null) {
            val pronunciations = dictionaryMapper.createPronunciations(input.pronunciations, output)
            output.pronunciations = pronunciations
        }
        if (input.definitions != null) {
            val definitions = dictionaryMapper.createDefinitions(input.definitions, output)
            output.definitions = definitions
        }
        if (input.examples != null) {
            val examples = dictionaryMapper.createExamples(input.examples, output, null)
            output.examples = examples
        }
        if (input.relations != null) {
            val relations = dictionaryMapper.createRelations(input.relations, output)
            output.relations = relations
        }
        return output
    }
}