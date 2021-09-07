package com.dreampany.common.data.source.pref

import android.content.Context
import com.dreampany.common.misc.constant.Constant
import com.dreampany.pref.Pref

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class Pref(val context: Context) {

    private val public: Pref
    private val private: Pref

    init {
        public = Pref(context)
        val prefName = getPrivateName(context)
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        private = Pref(pref)
    }

    open fun getPrivateName(context: Context): String = Constant.Keys.Pref.DEFAULT

    /* check */
    fun hasPublic(key: String): Boolean = public.contains(key)

    fun hasPrivate(key: String): Boolean = private.contains(key)

    /* check */
    fun removePublic(key: String) = public.remove(key)
    fun removePrivate(key: String) = private.remove(key)

    /* boolean */
    fun setPublicly(key: String, value: Boolean) = public.write(key, value)
    fun getPublicly(key: String, defaultValue: Boolean): Boolean = public.read(key, defaultValue)
    fun setPrivately(key: String, value: Boolean) = private.write(key, value)
    fun getPrivately(key: String, defaultValue: Boolean): Boolean = private.read(key, defaultValue)

    /* integer */
    fun setPublicly(key: String, value: Int) = public.write(key, value)
    fun getPublicly(key: String, defaultValue: Int): Int = public.read(key, defaultValue)

    /* long */
    fun setPrivately(key: String, value: Long) = private.write(key, value)
    fun getPrivately(key: String, defaultValue: Long): Long = private.read(key, defaultValue)


    /* string */
    fun setPublicly(key: String, value: String?) = public.write(key, value)
    fun getPublicly(key: String, defaultValue: String?): String? = public.read(key, defaultValue)

    fun setPrivately(key: String, value: String?) = private.write(key, value)
    fun getPrivately(key: String, defaultValue: String?): String? = private.read(key, defaultValue)

    /* custom */
    fun <T> setPublicly(key: String, value: T?) = public.write(key, value)
    fun <T> getPublicly(key: String, classOfT: Class<T>, defaultValue: T?): T? =
        public.read(key, classOfT, defaultValue)

    fun <T> setPrivately(key: String, value: T) = private.write(key, value)
    fun <T> getPrivately(key: String, classOfT: Class<T>, defaultValue: T?): T? = private.read(key, classOfT, defaultValue)
}