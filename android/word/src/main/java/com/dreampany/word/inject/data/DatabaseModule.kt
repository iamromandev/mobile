package com.dreampany.word.inject.data

import android.content.Context
import com.dreampany.word.data.source.room.dao.WordDao
import com.dreampany.word.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseManager = DatabaseManager(context)

    @Provides
    @Singleton
    fun provideWordDao(database: DatabaseManager): WordDao = database.wordDao()
}