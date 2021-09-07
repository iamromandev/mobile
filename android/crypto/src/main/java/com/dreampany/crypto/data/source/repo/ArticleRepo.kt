package com.dreampany.crypto.data.source.repo

import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.data.source.api.ArticleDataSource
import com.dreampany.crypto.data.source.mapper.NewsMapper
import com.dreampany.crypto.data.source.pref.NewsPref
import com.dreampany.framework.inject.annote.Remote
import com.dreampany.framework.inject.annote.Room
import com.dreampany.framework.misc.exts.append
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.RxMapper
import com.google.common.collect.Maps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 11/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ArticleRepo
@Inject constructor(
    rx: RxMapper,
    rm: ResponseMapper,
    private val pref: NewsPref,
    private val mapper: NewsMapper,
    @Room private val room: ArticleDataSource,
    @Remote private val remote: ArticleDataSource
) : ArticleDataSource {

    private val articles : MutableMap<String, List<Article>>

    init {
        articles = Maps.newConcurrentMap()
    }

    @Throws
    override suspend fun isFavorite(input: Article) = withContext(Dispatchers.IO) {
        room.isFavorite(input)
    }


    override suspend fun toggleFavorite(input: Article): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteArticles(): List<Article>? {
        TODO("Not yet implemented")
    }

    override suspend fun put(input: Article): Long {
        TODO("Not yet implemented")
    }

    override suspend fun put(inputs: List<Article>): List<Long>? {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String): Article? {
        TODO("Not yet implemented")
    }

    override suspend fun gets(): List<Article>? {
        TODO("Not yet implemented")
    }

    @Throws
    override suspend fun gets(
        query: String,
        language: String,
        offset: Long,
        limit: Long
    ) = withContext(Dispatchers.IO) {
        val id = append(query, language, offset)
        if (!articles.containsKey(id)) {
            val result = remote.gets(query, language, offset, limit)
            if (!result.isNullOrEmpty()) {
                //mapper.commitExpire(query, language, offset)
                //room.put(result)
                articles.put(append(query, language, offset), result)
            }
        }
        articles.get(append(query, language, offset))
        //room.gets(query, language, offset, limit)
    }

}