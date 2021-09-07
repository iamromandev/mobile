package com.dreampany.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.dreampany.pref.converter.GsonConverter
import com.dreampany.pref.converter.JsonConverter
import com.dreampany.pref.misc.value
import com.google.gson.reflect.TypeToken

/**
 * Created by roman on 7/20/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@SuppressLint("CommitPrefEdits")
class Pref(private val preferences: SharedPreferences, private val converter: JsonConverter) {

    constructor(context: Context) : this(
        PreferenceManager.getDefaultSharedPreferences(context),
        GsonConverter()
    )

    constructor(sharedPreferences: SharedPreferences) : this(sharedPreferences, GsonConverter())

    private val editor: SharedPreferences.Editor = preferences.edit()

    val size: Int get() = preferences.all.size

    fun clear() {
        if (size == 0) {
            return
        }
        editor.clear().apply()
    }

    fun remove(key: String) {
        if (!contains(key)) {
            return
        }
        editor.remove(key).apply()
    }

    fun contains(key: String): Boolean = preferences.contains(key)

    fun write(key: String, value: Boolean) = editor.putBoolean(key, value).apply()
    fun read(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)

    fun write(key: String, value: Int) = editor.putInt(key, value).apply()
    fun read(key: String, defaultValue: Int) = preferences.getInt(key, defaultValue)

    fun write(key: String, value: Long) = editor.putLong(key, value).apply()
    fun read(key: String, defaultValue: Long) = preferences.getLong(key, defaultValue)

    fun write(key: String, value: Float) = editor.putFloat(key, value).apply()
    fun read(key: String, defaultValue: Float) = preferences.getFloat(key, defaultValue)

    fun write(key: String, value: Double) = write(key, value.toString())
    fun read(key: String, defaultValue: Double) =
        read(key, defaultValue.toString())?.toDouble().value

    fun write(key: String, value: String?) = editor.putString(key, value).apply()
    fun read(key: String, defaultValue: String?) = preferences.getString(key, defaultValue)

    fun <T> write(key: String, value: T) {
        write(key, value, object : TypeToken<T>() {})
    }

    fun <T> write(key: String, value: T, typeTokenOfT: TypeToken<T>) {
        val json = converter.toJson(value, typeTokenOfT.type)
        write(key, json)
    }

     fun <T> read(key: String, classOfT: Class<T>, defaultValue: T?): T? {
         if (!contains(key) && defaultValue == null) return null
         return read(key, TypeToken.get(classOfT), defaultValue)
     }

     fun <T> read(key: String, typeTokenOfT: TypeToken<T>, defaultValue: T?): T? {
         val typeOfT = typeTokenOfT.type

         if (contains(key)) {
             return converter.fromJson(preferences.getString(key, null), typeOfT)
         } else {
             return defaultValue
         }
     }
}
