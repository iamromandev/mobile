package com.dreampany.translate.injector.ui

import com.dreampany.frame.misc.FragmentScope
import com.dreampany.translate.ui.fragment.MoreFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Hawladar Roman on 4/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
@Module
abstract class MoreModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun moreFragment(): MoreFragment
}