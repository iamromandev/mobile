package com.dreampany.word.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Pronunciation

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface PronunciationDao : BaseDao<Pronunciation>  {
    @Query("select * from pronunciation where word_id = :wordId")
    fun reads(wordId: String): List<Pronunciation>?
}