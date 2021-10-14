package com.dreampany.dictionary.inject.data

import com.dreampany.dictionary.data.source.remote.DictionaryService
import com.dreampany.dictionary.inject.qualifier.DictionaryServiceQualifier
import com.dreampany.dictionary.misc.constant.Constants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object DictionaryServiceModule {
    @Provides
    @DictionaryServiceQualifier
    fun provideDictionaryServiceRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.Values.DictionaryService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideDictionaryService(@DictionaryServiceQualifier retrofit: Retrofit) : DictionaryService =
        retrofit.create(DictionaryService::class.java)
}