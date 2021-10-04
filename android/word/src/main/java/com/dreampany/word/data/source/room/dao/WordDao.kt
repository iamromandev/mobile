package com.dreampany.word.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.word.data.model.Word

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface WordDao : BaseDao<Word>  {
    @Query("select * from word where word = :word limit 1")
    fun read(word: String): Word?
}