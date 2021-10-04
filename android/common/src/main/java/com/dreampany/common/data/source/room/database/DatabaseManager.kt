package com.dreampany.common.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.common.data.model.Store
import com.dreampany.common.data.source.room.converter.Converter
import com.dreampany.common.data.source.room.dao.StoreDao
import com.dreampany.common.misc.constant.Constant

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(entities = [Store::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun storeDao(): StoreDao

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
                    val database = Constant.database(context, Constant.Keys.Room.ROOM)
                    builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
                }

                return builder
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }


}