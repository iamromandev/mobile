package com.dreampany.nearby.data.source.memory

import com.dreampany.nearby.data.model.media.Apk
import com.dreampany.nearby.data.source.api.ApkDataSource
import com.dreampany.nearby.data.source.mapper.ApkMapper

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ApkMemoryDataSource(
    private val mapper: ApkMapper,
    private val provider: ApkProvider
) : ApkDataSource {

    override suspend fun isFavorite(input: Apk): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(input: Apk): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(): List<Apk>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Apk): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Apk>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Apk? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Apk>? =
        provider.gets()

    override suspend fun gets(ids: List<String>): List<Apk>? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(offset: Long, limit: Long): List<Apk>? {
        TODO("Not yet implemented")
    }
}