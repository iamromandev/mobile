package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.RelationType

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface RelationTypeDao : BaseDao<RelationType> {
    @Query("select count(*) from relationtype where id=:id")
    suspend fun count(id: String): Long

    @Query("select count(*) from relationtype where relation_type=:relationType")
    suspend fun countByRelationType(relationType: String): Long

    @Query("select * from relationtype where id=:id limit 1")
    fun read(id: String): RelationType?

    @Query("select * from relationtype where relation_type=:relationType limit 1")
    fun readByRelationType(relationType: String): RelationType?
}