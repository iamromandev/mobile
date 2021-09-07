package com.dreampany.hello.data.enums

import com.dreampany.framework.data.enums.BaseState
import com.dreampany.framework.misc.exts.title
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 16/4/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class State : BaseState {
    DEFAULT;

    override val value: String get() = name.title
}