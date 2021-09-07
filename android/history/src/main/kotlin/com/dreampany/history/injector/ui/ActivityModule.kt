package com.dreampany.history.injector.ui

import com.dreampany.history.ui.activity.LaunchActivity
import com.dreampany.history.ui.activity.NavigationActivity
import com.dreampany.history.ui.activity.ToolsActivity
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.activity.WebActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 5/23/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MoreModule::class, HomeModule::class, FavoriteModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class, HistoryModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity
}