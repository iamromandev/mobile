package com.dreampany.word.injector.ui

import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.activity.WebActivity
import com.dreampany.word.ui.activity.LaunchActivity
import com.dreampany.word.ui.activity.LoaderActivity
import com.dreampany.word.ui.activity.NavigationActivity
import com.dreampany.word.ui.activity.ToolsActivity
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
    @ContributesAndroidInjector
    abstract fun loaderActivity(): LoaderActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MoreModule::class, HomeModule::class, FavoritesModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, LicenseModule::class, AboutModule::class, WordModule::class, WordsVisionModule::class])
    abstract fun toolsActivity(): ToolsActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun webActivity(): WebActivity
}