package com.dreampany.word.data.source.repo

import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.word.data.model.*
import com.dreampany.word.data.source.api.DictionaryDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class DictionaryRepo
@Inject constructor(
    @Room private val room: DictionaryDataSource,
    @Remote private val remote: DictionaryDataSource
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


}