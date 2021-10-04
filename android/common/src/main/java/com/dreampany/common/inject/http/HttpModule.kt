package com.dreampany.common.inject.http

import android.content.Context
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.exts.isDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Module
@InstallIn(SingletonComponent::class)
object HttpModule {
    @Provides
    fun provideConnectionPool() : ConnectionPool = ConnectionPool()

    @Provides
    fun provideHttpClient(@ApplicationContext context: Context, pool: ConnectionPool): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (context.isDebug)
            interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(Constant.Values.Http.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.Values.Http.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(pool)
            .addInterceptor(interceptor)
            .build()

        return client
    }
}