package com.dreampany.word.data.source.api

import com.dreampany.word.data.model.Word

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface WordDataSource {
    suspend fun read(word: String): Word?
}