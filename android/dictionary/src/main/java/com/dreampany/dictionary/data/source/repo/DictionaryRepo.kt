package com.dreampany.dictionary.data.source.repo

import com.dreampany.common.inject.qualifier.Remote
import com.dreampany.common.inject.qualifier.Room
import com.dreampany.dictionary.data.source.api.DictionaryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Singleton
class DictionaryRepo
@Inject constructor(
    @Room private val room: DictionaryDataSource,
    @Remote private val remote: DictionaryDataSource
) : DictionaryDataSource {


    override suspend fun read(word: String)= withContext(Dispatchers.IO) {
        remote.read(word)
    }


}