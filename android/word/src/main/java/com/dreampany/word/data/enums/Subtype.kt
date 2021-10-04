package com.dreampany.word.data.enums

import com.dreampany.common.data.enums.BaseEnum
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 10/5/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
enum class Subtype : BaseEnum {
    DEFAULT,
    APPS,
    RATE_US,
    FEEDBACK,
    INVITE,
    LICENSE,
    ABOUT;

    override val value: String get() = name
}