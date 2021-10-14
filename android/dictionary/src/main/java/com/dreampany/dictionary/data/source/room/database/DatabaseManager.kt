package com.dreampany.dictionary.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.common.misc.constant.Constant
import com.dreampany.dictionary.data.model.*
import com.dreampany.dictionary.data.source.room.dao.*
import com.dreampany.dictionary.misc.constant.Constants

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(
    entities = [
        Language::class,
        Source::class,
        PartOfSpeech::class,
        Word::class,
        Pronunciation::class,
        Definition::class,
        Example::class,
        RelationType::class,
        Relation::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun languageDao(): LanguageDao
    abstract fun sourceDao(): SourceDao
    abstract fun partOfSpeechDao(): PartOfSpeechDao
    abstract fun wordDao(): WordDao
    abstract fun pronunciationDao(): PronunciationDao
    abstract fun definitionDao(): DefinitionDao
    abstract fun exampleDao(): ExampleDao
    abstract fun relationTypeDao(): RelationTypeDao
    abstract fun relationDao(): RelationDao

    companion object {
        private val lock = Any()

        @Volatile
        private var instance: DatabaseManager? = null

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: newInstance(context, false).also { instance = it }
        }

        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager =
            synchronized(lock) {
                val builder: Builder<DatabaseManager>

                if (memoryOnly) {
                    builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
                } else {
                    val database = Constant.database(context, Constants.Values.Room.ROOM)
                    builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
                }

                return builder
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}