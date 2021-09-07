package com.dreampany.network.nearby.data.source.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dreampany.network.misc.Constant
import com.dreampany.network.nearby.data.model.Packet
import com.dreampany.network.nearby.data.source.room.dao.PacketDao

/**
 * Created by roman on 7/31/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Database(entities = [Packet::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {
    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val database = Constant.database(context, Constant.Room.TYPE_NEARBY)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, database)
            }

            return builder
                .fallbackToDestructiveMigration()
                .build()
        }

        @Synchronized
        fun instanceOf(context: Context): DatabaseManager {
            if (instance == null)
                instance = newInstance(context, false)
            return instance!!
        }
    }

    abstract fun packetDao(): PacketDao
}