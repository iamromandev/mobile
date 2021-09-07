package com.dreampany.hi.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.common.misc.constant.Constant
import com.dreampany.hi.data.model.File
import com.dreampany.hi.data.model.Message
import com.dreampany.hi.data.source.room.dao.FileDao
import com.dreampany.hi.data.source.room.dao.MessageDao
import com.dreampany.hi.misc.constant.Constants

/**
 * Created by roman on 8/21/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(entities = [Message::class, File::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun fileDao(): FileDao

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
                    val database = Constant.database(context, Constants.Keys.Room.ROOM)
                    builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
                }

                return builder
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}