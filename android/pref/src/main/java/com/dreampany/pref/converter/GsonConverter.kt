package com.dreampany.pref.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * Created by roman on 7/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class GsonConverter
constructor(
   private val gson: Gson
) : JsonConverter {

    constructor(): this(GsonBuilder().create())

    override fun <T> fromJson(json: String?, typeOfT: Type): T =
        gson.fromJson(json, typeOfT)

    override fun <T> toJson(input: T, typeOfT: Type): String =
        gson.toJson(input, typeOfT)
}