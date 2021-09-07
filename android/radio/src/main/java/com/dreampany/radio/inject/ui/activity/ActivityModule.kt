package com.dreampany.radio.inject.ui.activity

import com.dreampany.radio.ui.web.WebActivity
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.radio.ui.page.PagesActivity
import com.dreampany.radio.inject.ui.home.HomeModule
import com.dreampany.radio.inject.ui.more.MoreModule
import com.dreampany.radio.inject.ui.settings.SettingsModule
import com.dreampany.radio.ui.home.activity.HomeActivity
import com.dreampany.radio.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by roman on 3/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ContributesAndroidInjector
    abstract fun web(): WebActivity

    @ContributesAndroidInjector
    abstract fun pages(): PagesActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class, MoreModule::class, SettingsModule::class])
    abstract fun home(): HomeActivity
}