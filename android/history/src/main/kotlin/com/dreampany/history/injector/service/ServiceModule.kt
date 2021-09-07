package com.dreampany.history.injector.service

import com.dreampany.history.service.NotifyService
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun notifyService(): NotifyService
}