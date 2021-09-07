package com.dreampany.tools.api.news.inject.data

import com.dreampany.tools.api.news.inject.NewsApiAnnote
import com.dreampany.tools.api.news.misc.Constants
import com.dreampany.tools.api.news.remote.service.NewsApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 8/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class NewsApiModule {
    @Singleton
    @Provides
    @NewsApiAnnote
    fun provide(gson: Gson, httpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.Apis.NewsApi.BASE_URL)
            .client(httpClient)
            .build()

    @Singleton
    @Provides
    fun provideService(@NewsApiAnnote retrofit: Retrofit) : NewsApiService =
        retrofit.create(NewsApiService::class.java)
}