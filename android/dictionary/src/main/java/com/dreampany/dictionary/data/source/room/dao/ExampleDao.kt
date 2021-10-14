package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.Example

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface ExampleDao : BaseDao<Example> {
    @Query("select count(*) from example where id=:id")
    suspend fun count(id: String): Long

    @Query("select * from example where id=:id limit 1")
    suspend fun read(id: String): Example?

    @Query("select * from example where word_id=:wordId and definition_id is null")
    suspend fun reads(wordId: String): List<Example>?

    @Query("select * from example where definition_id=:definitionId")
    suspend fun readsByDefinition(definitionId: String): List<Example>?
}