package com.dreampany.word.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Definition

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface DefinitionDao : BaseDao<Definition>  {
    @Query("select count(*) from definition where id=:id")
    suspend fun count(id: String): Long

    @Query("select * from definition where id=:id limit 1")
    suspend fun read(id: String): Definition?
}