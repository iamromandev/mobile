package com.dreampany.hi.data.enums

import com.dreampany.common.data.enums.BaseEnum
import kotlinx.parcelize.Parcelize


/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
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
    ABOUT,
    TEXT;

    override val value: String get() = name
}