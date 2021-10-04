package com.dreampany.word.inject.data
import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.remote.DictionaryService
import com.dreampany.word.data.source.remote.WordRemoteDataSource
import com.dreampany.word.data.source.room.WordRoomDataSource
import com.dreampany.word.data.source.room.dao.WordDao
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
object WordModule {
    @Provides
    @Room
    fun room(
        dao: WordDao
    ): WordDataSource = WordRoomDataSource(dao)


    @Provides
    @Remote
    fun remote(
        dictionaryService: DictionaryService
    ): WordDataSource = WordRemoteDataSource(dictionaryService)
}