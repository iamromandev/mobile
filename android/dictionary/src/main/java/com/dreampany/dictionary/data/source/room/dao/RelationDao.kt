package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.Relation

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface RelationDao : BaseDao<Relation> {
    @Query("select count(*) from relation where left_word_id=:leftWordId and right_word_id=:rightWordId")
    suspend fun count(leftWordId: String, rightWordId: String): Long

    @Query("select * from relation where left_word_id=:leftWordId and right_word_id=:rightWordId limit 1")
    suspend fun read(leftWordId: String, rightWordId: String): Relation?

    @Query("select * from relation where left_word_id=:wordId or right_word_id=:wordId")
    suspend fun reads(wordId: String): List<Relation>?
}