package com.dreampany.word.data.source.room

import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource
import com.dreampany.word.data.source.room.dao.WordDao

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class WordRoomDataSource
constructor(
    private val dao: WordDao
) : WordDataSource {

    override suspend fun read(word: String): Word? = dao.read(word)
}