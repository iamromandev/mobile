package com.dreampany.word.data.source.remote

import com.dreampany.word.data.model.Word
import com.dreampany.word.data.source.api.WordDataSource

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class WordRemoteDataSource
constructor(
    private val dictionaryService: DictionaryService
) : WordDataSource {
    override suspend fun read(word: String): Word? {
        val response = dictionaryService.getWord(word)
        if (response.isSuccessful) {
            val data = response.body() ?: return null
            return Word(id=data.id, word=data.word)
        }
        return null
    }
}