package com.dreampany.dictionary.data.source.api

import com.dreampany.dictionary.data.model.*

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface DictionaryDataSource {
    suspend fun read(word: String): Word?
}