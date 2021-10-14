package com.dreampany.dictionary.data.source.room

import com.dreampany.dictionary.data.model.*
import com.dreampany.dictionary.data.source.api.DictionaryDataSource
import com.dreampany.dictionary.data.source.mapper.DictionaryMapper
import com.dreampany.dictionary.data.source.room.dao.*

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class DictionaryRoomDataSource
constructor(
    private val dictionaryMapper: DictionaryMapper,
    private val languageDao: LanguageDao,
    private val sourceDao: SourceDao,
    private val partOfSpeechDao: PartOfSpeechDao,
    private val wordDao: WordDao,
    private val pronunciationDao: PronunciationDao,
    private val definitionDao: DefinitionDao,
    private val exampleDao: ExampleDao,
    private val relationTypeDao: RelationTypeDao,
    private val relationDao: RelationDao
) : DictionaryDataSource {



    override suspend fun read(word: String): Word? {
        TODO("Not yet implemented")
    }


}