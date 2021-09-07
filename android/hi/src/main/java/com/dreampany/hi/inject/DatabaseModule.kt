package com.dreampany.hi.inject

import android.content.Context
import com.dreampany.hi.data.source.room.dao.MessageDao
import com.dreampany.hi.data.source.room.database.DatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by roman on 8/21/21
 * Copyright (c) 2021 butler. All rights reserved.
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
    fun provideMessageDao(database: DatabaseManager): MessageDao = database.messageDao()

}