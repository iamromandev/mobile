package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.Source

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface SourceDao : BaseDao<Source> {
    @Query("select count(*) from source where id=:id")
    suspend fun count(id: String): Long

    @Query("select count(*) from source where source=:source")
    suspend fun countBySource(source: String): Long

    @Query("select * from source where id=:id limit 1")
    suspend fun read(id: String): Source?

    @Query("select * from source where source=:source limit 1")
    suspend fun readBySource(source: String): Source?
}