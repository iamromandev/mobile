package com.dreampany.framework.inject.http

import android.content.Context
import com.dreampany.framework.misc.constant.Constant
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class HttpModule {

    @Provides
    @Singleton
    fun provideConnectionPool() : ConnectionPool {
        return ConnectionPool()
    }

    @Provides
    @Singleton
    fun provideHttpClient(context: Context, pool: ConnectionPool): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        //if (context.isDebug)
            interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(Constant.Http.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.Http.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(pool)
            .addInterceptor(interceptor)
            .build()

        return client
    }
}