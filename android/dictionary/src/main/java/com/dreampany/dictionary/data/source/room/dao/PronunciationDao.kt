package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.Pronunciation

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface PronunciationDao : BaseDao<Pronunciation>  {
    @Query("select count(*) from pronunciation where id=:id")
    suspend fun count(id: String): Long

    @Query("select * from pronunciation where id=:id limit 1")
    suspend fun read(id: String): Pronunciation?
}