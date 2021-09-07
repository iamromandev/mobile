package com.dreampany.radio.inject.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.inject.annote.ViewModelKey
import com.dreampany.framework.ui.vm.factory.ViewModelFactory
import com.dreampany.radio.ui.more.vm.MoreViewModel
import com.dreampany.radio.ui.vm.PageViewModel
import com.dreampany.radio.ui.vm.SearchViewModel
import com.dreampany.radio.ui.vm.StationViewModel
import com.dreampany.radio.ui.vm.TimeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMore(vm: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimeViewModel::class)
    abstract fun bindTime(vm: TimeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearch(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PageViewModel::class)
    abstract fun bindPage(vm: PageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StationViewModel::class)
    abstract fun bindStation(vm: StationViewModel): ViewModel
}