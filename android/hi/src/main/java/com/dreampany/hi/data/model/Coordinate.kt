package com.dreampany.hi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by roman on 8/28/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Coordinate(
    val latitude: Double,
    val longitude: Double
) : Parcelable