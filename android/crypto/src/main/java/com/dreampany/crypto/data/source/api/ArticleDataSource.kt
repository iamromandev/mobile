package com.dreampany.crypto.data.source.api

import com.dreampany.crypto.data.model.Article

/**
 * Created by roman on 3/21/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface ArticleDataSource {

    @Throws
    suspend fun isFavorite(input: Article): Boolean

    @Throws
    suspend fun toggleFavorite(input: Article): Boolean

    @Throws
    suspend fun getFavoriteArticles(): List<Article>?

    @Throws
    suspend fun put(input: Article): Long

    @Throws
    suspend fun put(inputs: List<Article>): List<Long>?

    @Throws
    suspend fun get(id: String): Article?

    @Throws
    suspend fun gets(): List<Article>?

    @Throws
    suspend fun gets(query: String, language: String, offset: Long, limit: Long): List<Article>?
}