package com.dreampany.word.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Relation

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface RelationDao : BaseDao<Relation> {
    @Query("select * from relation where left_word_id = :wordId or right_word_id = :wordId")
    fun reads(wordId: String): List<Relation>?
}