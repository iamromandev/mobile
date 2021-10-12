package com.dreampany.word.data.source.api

import com.dreampany.word.data.model.*

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface DictionaryDataSource {

    suspend fun write(input: Language): Long

    suspend fun write(input: Source): Long

    suspend fun write(input: PartOfSpeech): Long

    suspend fun write(input: Word): Long

    suspend fun write(input: Pronunciation): Long

    suspend fun writePronunciations(inputs: List<Pronunciation>): List<Long>

    suspend fun write(input: Definition): Long

    suspend fun writeDefinitions(inputs: List<Definition>): List<Long>

    suspend fun write(input: Example): Long

    suspend fun writeExamples(inputs: List<Example>): List<Long>

    suspend fun write(input: RelationType): Long

    suspend fun write(input: Relation): Long

    suspend fun writeRelations(inputs: List<Relation>): List<Long>

    suspend fun read(word: String): Word?
}