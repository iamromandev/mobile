package com.dreampany.word.data.source.repo

import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class WordRepo
@Inject constructor(
    @Room private val room: WordDataSource,
    @Remote private val remote: WordDataSource
) : WordDataSource {

    override suspend fun read(word: String): Word? {
        val word = remote.read(word)
        return word
    }

}