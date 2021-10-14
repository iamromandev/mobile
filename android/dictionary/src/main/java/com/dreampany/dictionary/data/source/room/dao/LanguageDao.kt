package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.Language

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface LanguageDao : BaseDao<Language> {
    @Query("select count(*) from language where id=:id")
    suspend fun count(id: String): Long

    @Query("select count(*) from language where code=:code and name=:name")
    suspend fun count(code: String, name: String): Long

    @Query("select * from language where id=:id limit 1")
    suspend fun read(id: String): Language?

    @Query("select * from language where code=:code and name=:name limit 1")
    suspend fun read(code: String, name: String): Language?
}