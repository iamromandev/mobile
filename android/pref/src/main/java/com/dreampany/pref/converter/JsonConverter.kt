package com.dreampany.pref.converter

import java.lang.reflect.Type

/**
 * Created by roman on 7/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface JsonConverter {
    fun <T> fromJson(json: String?, typeOfT: Type): T

    fun <T> toJson(input: T, typeOfT: Type): String
}