package com.dreampany.history.data.source.room

import androidx.room.TypeConverter
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.frame.data.model.Link
import com.dreampany.history.data.enums.HistorySource
import com.dreampany.history.data.enums.LinkSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Roman-372 on 7/25/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Converters {

    private val gson = Gson()
    private val typeOfLinks = object : TypeToken<List<Link>>() {}.type

    @TypeConverter
    @Synchronized
    fun toHistorySourceString(source: HistorySource?): String? {
        return source?.name
    }

    @TypeConverter
    @Synchronized
    fun toHistorySource(json: String?): HistorySource? {
        return if (json.isNullOrEmpty()) null else HistorySource.valueOf(json)
    }

    @TypeConverter
    @Synchronized
    fun toLinkSourceString(source: LinkSource?): String? {
        return source?.name
    }

    @TypeConverter
    @Synchronized
    fun toLinkSource(json: String?): LinkSource? {
        return if (json.isNullOrEmpty()) null else LinkSource.valueOf(json)
    }

    @TypeConverter
    @Synchronized
    fun toHistoryTypeString(type: HistoryType?): String? {
        return type?.name
    }

    @TypeConverter
    @Synchronized
    fun toHistoryType(json: String?): HistoryType? {
        return if (json.isNullOrEmpty()) null else HistoryType.valueOf(json)
    }

    @TypeConverter
    @Synchronized
    fun fromLinks(links: MutableList<Link>): String {
        return gson.toJson(links, typeOfLinks)
    }

    @TypeConverter
    @Synchronized
    fun fromLinksJson(json: String): MutableList<Link> {
        return gson.fromJson<MutableList<Link>>(json, typeOfLinks)
    }
}