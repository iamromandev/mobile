package com.dreampany.dictionary.data.enums

import com.dreampany.common.data.enums.BaseEnum
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class State : BaseEnum {
    DEFAULT, FAVORITE;

    override val value: String get() = name
}