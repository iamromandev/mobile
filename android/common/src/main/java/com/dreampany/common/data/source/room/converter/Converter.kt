package com.dreampany.common.data.source.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 7/14/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
open class Converter {

    //protected val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    protected val gson = Gson()
    private val type = object : TypeToken<List<String>>() {}.type

    /*@TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }*/

    @TypeConverter
    fun toString(values: List<String>?): String? {
        return if (values.isNullOrEmpty()) null
        else gson.toJson(values, type)
    }

    @TypeConverter
    fun fromString(json: String?): List<String>? {
        return if (json.isNullOrEmpty()) null
        else gson.fromJson<List<String>>(json, type)
    }

}