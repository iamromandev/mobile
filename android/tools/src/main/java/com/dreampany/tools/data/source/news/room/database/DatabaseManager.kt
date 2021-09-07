package com.dreampany.tools.data.source.news.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.data.model.news.Article
import com.dreampany.tools.data.model.news.Page
import com.dreampany.tools.data.source.news.room.converters.Converters
import com.dreampany.tools.data.source.news.room.dao.ArticleDao
import com.dreampany.tools.data.source.news.room.dao.PageDao
import com.dreampany.tools.misc.constants.Constants

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Database(entities = [Article::class, Page::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {

    companion object {
        private var instance: DatabaseManager? = null

        @Synchronized
        fun newInstance(context: Context, memoryOnly: Boolean): DatabaseManager {
            val builder: RoomDatabase.Builder<DatabaseManager>

            if (memoryOnly) {
                builder = Room.inMemoryDatabaseBuilder(context, DatabaseManager::class.java)
            } else {
                val DATABASE = Constant.database(context, Constants.Keys.Room.NEWS)
                builder = Room.databaseBuilder(context, DatabaseManager::class.java, DATABASE)
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

    abstract fun pageDao(): PageDao

    abstract fun articleDao(): ArticleDao
}