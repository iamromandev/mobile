package com.dreampany.common.misc.exts

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowInsets
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constant
import kotlin.reflect.KClass

/**
 * Created by roman on 7/19/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
fun <T : Any> Fragment?.open(target: KClass<T>, finishCurrent: Boolean = false) {
    this?.run {
        startActivity(Intent(activity, target.java))
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Any> Fragment?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>? = null,
    finishCurrent: Boolean = false
) {
    this?.run {
        val intent = Intent(activity, target.java)
        task?.let {
            intent.putExtra(Constant.Keys.TASK, it as Parcelable)
        }
        startActivity(intent)
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Any> Fragment?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>,
    requestCode: Int,
    finishCurrent: Boolean = false
) {
    this?.run {
        val intent = Intent(activity, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivityForResult(intent, requestCode)
        if (finishCurrent) {
            activity?.finish()
        }
    }
}

fun <T : Fragment> createFragment(clazz: KClass<T>, task: Task<*, *, *, *, *>): T {
    val instance = clazz.java.newInstance()
    if (instance.arguments == null) {
        val bundle = Bundle()
        bundle.putParcelable(Constant.Keys.TASK, task)
        instance.arguments = bundle
    } else {
        instance.arguments?.putParcelable(Constant.Keys.TASK, task)
    }
    return instance
}

val Fragment?.bundle: Bundle?
    get() = this?.arguments

val Fragment?.task: Task<*, *, *, *, *>?
    get() {
        val bundle = this.bundle ?: return null
        return bundle.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?
    }

inline fun <reified T : View> Fragment?.findViewById(@IdRes id: Int): T? =
    this?.view?.findViewById(id)

@ColorInt
fun Fragment?.color(@ColorRes resId: Int): Int =
    this?.requireContext().color(resId)

val Fragment?.contextRef: Context?
    get() {
        if (this == null) return null
        if (this.context != null) return context
        if (this.view?.context != null) return this.view?.context
        if (this.parentFragment?.context != null) return this.parentFragment?.context
        if (this.activity.context != null) return this.activity.context
        return null
    }

val Fragment?.packageName: String get() = contextRef.packageName

val Fragment?.versionCode: Long get() = contextRef.versionCode

val Fragment?.versionName: String get() = contextRef.versionName

fun Fragment.hideKeyboard() = activity?.hideKeyboard()

inline val Fragment.windowWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.width() - insets.left - insets.right
        } else {
            val view = requireActivity().window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.widthPixels - insets.left - insets.right
        }
    }

inline val Fragment.windowHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.height() - insets.bottom - insets.top
        } else {
            val view = requireActivity().window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.heightPixels - insets.bottom - insets.top
        }
    }