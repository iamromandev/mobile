package com.dreampany.hello.inject.app

import android.app.Application
import com.dreampany.framework.inject.app.AppModule
import com.dreampany.framework.inject.property.PropertyModule
import com.dreampany.hello.app.App
import com.dreampany.hello.inject.data.DataModule
import com.dreampany.hello.inject.ui.activity.ActivityModule
import com.dreampany.hello.inject.ui.vm.ViewModelModule
import com.dreampany.hello.inject.worker.WorkerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by roman on 26/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        PropertyModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        WorkerModule::class,
        DataModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}