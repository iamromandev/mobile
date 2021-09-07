package com.dreampany.radio.data.enums

import com.dreampany.framework.data.enums.BaseType
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Type : BaseType {
    DEFAULT, MORE, SITE, SEARCH, PAGE, STATION;

    override val value: String get() = name
}