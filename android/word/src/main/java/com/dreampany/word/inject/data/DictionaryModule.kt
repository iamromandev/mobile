package com.dreampany.word.inject.data
import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.word.data.source.api.DictionaryDataSource
import com.dreampany.word.data.source.mapper.DictionaryMapper
import com.dreampany.word.data.source.remote.DictionaryService
import com.dreampany.word.data.source.remote.DictionaryRemoteDataSource
import com.dreampany.word.data.source.room.DictionaryRoomDataSource
import com.dreampany.word.data.source.room.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object DictionaryModule {
    @Room
    @Provides
    fun room(
        dictionaryMapper: DictionaryMapper,
        languageDao: LanguageDao,
        sourceDao: SourceDao,
        partOfSpeechDao: PartOfSpeechDao,
        wordDao: WordDao,
        pronunciationDao: PronunciationDao,
        definitionDao: DefinitionDao,
        exampleDao: ExampleDao,
        relationTypeDao: RelationTypeDao,
        relationDao: RelationDao
    ): DictionaryDataSource = DictionaryRoomDataSource(
        dictionaryMapper,
        languageDao,
        sourceDao,
        partOfSpeechDao,
        wordDao,
        pronunciationDao,
        definitionDao,
        exampleDao,
        relationTypeDao,
        relationDao
    )

    @Remote
    @Provides
    fun remote(
        dictionaryService: DictionaryService
    ): DictionaryDataSource = DictionaryRemoteDataSource(dictionaryService)
}