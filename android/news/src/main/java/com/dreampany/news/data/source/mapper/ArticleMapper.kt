package com.dreampany.news.data.source.mapper

import com.dreampany.framework.data.source.mapper.StoreMapper
import com.dreampany.framework.data.source.repo.StoreRepo
import com.dreampany.framework.misc.exts.*
import com.dreampany.news.api.news.model.NewsArticle
import com.dreampany.news.data.enums.State
import com.dreampany.news.data.enums.Subtype
import com.dreampany.news.data.enums.Type
import com.dreampany.news.data.model.Article
import com.dreampany.news.data.source.api.ArticleDataSource
import com.dreampany.news.data.source.pref.Prefs
import com.dreampany.news.misc.Constants
import com.google.common.collect.Maps
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ArticleMapper
@Inject constructor(
    private val storeMapper: StoreMapper,
    private val storeRepo: StoreRepo,
    private val pref: Prefs
) {
    private val articles: MutableMap<String, Article>
    private val favorites: MutableMap<String, Boolean>

    init {
        articles = Maps.newConcurrentMap()
        favorites = Maps.newConcurrentMap()
    }

    @Synchronized
    fun isExpired(query: String, language: String, offset: Long): Boolean {
        val time = pref.getExpireTime(query, language, offset)
        return time.isExpired(Constants.Times.NEWS)
    }

    @Synchronized
    fun commitExpire(query: String, language: String, offset: Long) =
        pref.commitExpireTime(query, language, offset)

    @Synchronized
    fun add(input: Article) = articles.put(input.id, input)

    @Throws
    suspend fun isFavorite(input: Article): Boolean {
        if (!favorites.containsKey(input.id)) {
            val favorite = storeRepo.isExists(
                input.id,
                Type.ARTICLE.value,
                Subtype.DEFAULT.value,
                State.FAVORITE.value
            )
            favorites.put(input.id, favorite)
        }
        return favorites.get(input.id).value
    }

    @Throws
    suspend fun insertFavorite(input: Article): Boolean {
        favorites.put(input.id, true)
        val store = storeMapper.readStore(
            input.id,
            Type.ARTICLE.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.write(it) }
        return true
    }

    @Throws
    suspend fun deleteFavorite(input: Article): Boolean {
        favorites.put(input.id, false)
        val store = storeMapper.readStore(
            input.id,
            Type.ARTICLE.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        store?.let { storeRepo.delete(it) }
        return false
    }

    @Throws
    @Synchronized
    suspend fun getItems(
        query: String,
        language: String,
        offset: Long,
        limit: Long,
        source: ArticleDataSource
    ): List<Article>? {
        updateCache(source)
        val cache = sortedArticles(articles.values.toList())
        val result = sub(cache, offset, limit)
        return result
    }

    @Throws
    @Synchronized
    suspend fun getFavoriteItems(
        source: ArticleDataSource
    ): List<Article>? {
        updateCache(source)
        val stores = storeRepo.reads(
            Type.ARTICLE.value,
            Subtype.DEFAULT.value,
            State.FAVORITE.value
        )
        val outputs = stores?.mapNotNull { input -> articles.get(input.id) }
        var result: List<Article>? = null
        outputs?.let {
            result = sortedArticles(it)
        }
        result?.forEach {
            //bindQuote(currency, it, quoteDao)
        }
        return result
    }

    @Synchronized
    fun getItems(inputs: List<NewsArticle>): List<Article> {
        val result = arrayListOf<Article>()
        inputs.forEach { input ->
            result.add(getItem(input))
        }
        return result
    }

    @Synchronized
    fun getItem(input: NewsArticle): Article {
        Timber.v("Resolved Article: %s", input.url);
        val id = input.url
        var out: Article? = articles.get(id)
        if (out == null) {
            out = Article(id)
            articles.put(id, out)
        }
        out.source = getSource(input.source)
        out.author = input.author
        out.title = input.title
        out.description = input.description
        out.content = input.content
        out.url = input.url
        out.imageUrl = input.imageUrl
        out.publishedAt = input.publishedAt.simpleUtc
        return out
    }

    @Synchronized
    fun getSource(input: NewsArticle.Source): Article.Source {
        return Article.Source(input.id, input.name)
    }

    @Throws
    @Synchronized
    private suspend fun updateCache(source: ArticleDataSource) {
        if (articles.isEmpty()) {
            source.gets()?.let {
                if (it.isNotEmpty())
                    it.forEach { add(it) }
            }
        }
    }

    @Synchronized
    private fun sortedArticles(
        inputs: List<Article>
    ): List<Article> {
        val temp = ArrayList(inputs)
        val comparator = ArticleComparator()
        temp.sortWith(comparator)
        return temp
    }

    class ArticleComparator : Comparator<Article> {
        override fun compare(left: Article, right: Article): Int {
            return right.publishedAt.compareTo(left.publishedAt)
        }
    }

}