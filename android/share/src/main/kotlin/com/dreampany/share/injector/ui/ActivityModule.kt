package com.dreampany.share.injector.ui

import com.dreampany.frame.misc.ActivityScope
import com.dreampany.lca.injector.ui.LicenseModule
import com.dreampany.share.ui.activity.LaunchActivity
import com.dreampany.share.ui.activity.NavigationActivity
import com.dreampany.share.ui.activity.ToolsActivity
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
    @ContributesAndroidInjector(modules = [DiscoverModule::class, ShareModule::class, DownloadModule::class, MoreModule::class])
    abstract fun navigationActivity(): NavigationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MediaModule::class, SettingsModule::class, LicenseModule::class, AboutModule::class])
    abstract fun toolsActivity(): ToolsActivity
}