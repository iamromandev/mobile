package com.dreampany.word.inject.data

import android.content.Context
import com.dreampany.word.data.source.room.dao.*
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
    fun provideLanguageDao(database: DatabaseManager): LanguageDao = database.languageDao()

    @Provides
    @Singleton
    fun provideSourceDao(database: DatabaseManager): SourceDao = database.sourceDao()

    @Provides
    @Singleton
    fun providePartOfSpeechDao(database: DatabaseManager): PartOfSpeechDao = database.partOfSpeechDao()

    @Provides
    @Singleton
    fun provideWordDao(database: DatabaseManager): WordDao = database.wordDao()

    @Provides
    @Singleton
    fun providePronunciationDao(database: DatabaseManager): PronunciationDao = database.pronunciationDao()

    @Provides
    @Singleton
    fun provideDefinitionDao(database: DatabaseManager): DefinitionDao = database.definitionDao()

    @Provides
    @Singleton
    fun provideExampleDao(database: DatabaseManager): ExampleDao = database.exampleDao()

    @Provides
    @Singleton
    fun provideRelationTypeDao(database: DatabaseManager): RelationTypeDao = database.relationTypeDao()

    @Provides
    @Singleton
    fun provideRelationDao(database: DatabaseManager): RelationDao = database.relationDao()

}