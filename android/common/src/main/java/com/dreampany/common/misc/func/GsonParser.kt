package com.dreampany.common.misc.func

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Created by roman on 7/22/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class GsonParser
@Inject constructor(
    val gson: Gson
) {
    inline fun <reified T> toJson(value: T) = gson.toJson(value, object : TypeToken<T>() {}.type)
    inline fun <reified T> fromJson(json: String) = gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
}