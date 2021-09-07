package com.dreampany.framework.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.data.model.Store
import com.dreampany.framework.data.model.Time
import com.dreampany.framework.data.source.room.converter.Converter
import com.dreampany.framework.data.source.room.dao.StoreDao
import com.dreampany.framework.data.source.room.dao.TimeDao
import com.dreampany.framework.misc.constant.Constant

/**
 * Created by roman on 1/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Store::class, Time::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val database = Constant.database(context, Constant.Room.TYPE_FRAMEWORK)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun getInstance(context: Context): DatabaseManager {
            if (instance == null) {
                instance =
                    newInstance(
                        context,
                        false
                    )
            }
            return instance!!
        }
    }

    abstract fun storeDao(): StoreDao
    abstract fun timeDao(): TimeDao
}