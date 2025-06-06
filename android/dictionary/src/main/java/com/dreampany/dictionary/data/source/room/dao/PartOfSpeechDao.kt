package com.dreampany.dictionary.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dreampany.common.data.source.room.dao.BaseDao
import com.dreampany.dictionary.data.model.PartOfSpeech

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Dao
interface PartOfSpeechDao : BaseDao<PartOfSpeech> {
    @Query("select count(*) from partofspeech where id=:id")
    suspend fun count(id: String): Long

    @Query("select count(*) from partofspeech where part_of_speech=:partOfSpeech")
    suspend fun countByPartOfSpeech(partOfSpeech: String): Long

    @Query("select * from partofspeech where id=:id limit 1")
    fun read(id: String): PartOfSpeech?

    @Query("select * from partofspeech where part_of_speech=:partOfSpeech limit 1")
    fun readByPartOfSpeech(partOfSpeech: String): PartOfSpeech?
}