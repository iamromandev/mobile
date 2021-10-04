package com.dreampany.word.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.common.misc.constant.Constant
import com.dreampany.word.data.model.Language
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.room.dao.WordDao
import com.dreampany.word.misc.constant.Constants

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(entities = [Language::class, Word::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun wordDao(): WordDao

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