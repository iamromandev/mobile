package com.dreampany.hello.inject.data

import android.content.Context
import com.dreampany.framework.inject.annote.Database
import com.dreampany.framework.inject.annote.Firestore
import com.dreampany.hello.data.source.api.UserDataSource
import com.dreampany.hello.data.source.database.UserDatabaseDataSource
import com.dreampany.hello.manager.FirestoreManager
import com.dreampany.hello.data.source.firestore.UserFirestoreDataSource
import com.dreampany.hello.data.source.mapper.UserMapper
import com.dreampany.hello.manager.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by roman on 26/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
class UserModule {

    @Singleton
    @Provides
    @Firestore
    fun firestore(
        context: Context,
        mapper: UserMapper,
        firestore: FirestoreManager
    ): UserDataSource = UserFirestoreDataSource(context, mapper, firestore)

    @Singleton
    @Provides
    @Database
    fun database(
        context: Context,
        mapper: UserMapper,
        database: DatabaseManager
    ): UserDataSource = UserDatabaseDataSource(context, mapper, database)
}