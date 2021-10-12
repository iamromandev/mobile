package com.dreampany.word.data.source.remote

import com.dreampany.word.data.model.*
import com.dreampany.word.data.source.api.DictionaryDataSource

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class DictionaryRemoteDataSource
constructor(
    private val dictionaryService: DictionaryService
) : DictionaryDataSource {
    override suspend fun write(input: Language): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Source): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: PartOfSpeech): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Word): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Pronunciation): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Definition): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Example): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: RelationType): Long {
        TODO("Not yet implemented")
    }

    override suspend fun write(input: Relation): Long {
        TODO("Not yet implemented")
    }

    override suspend fun writePronunciations(inputs: List<Pronunciation>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun writeDefinitions(inputs: List<Definition>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun writeExamples(inputs: List<Example>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun writeRelations(inputs: List<Relation>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun read(word: String): Word? {
        TODO("Not yet implemented")
    }
/*    override suspend fun read(word: String): Word? {
        val response = dictionaryService.getWord(word)
        if (response.isSuccessful) {
            val data = response.body() ?: return null
            return Word(id=data.id, word=data.word)
        }
        return null
    }*/
}