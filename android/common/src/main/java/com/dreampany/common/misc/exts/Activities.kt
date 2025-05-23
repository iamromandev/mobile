package com.dreampany.common.misc.exts

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Task
import com.dreampany.common.misc.constant.Constant
import com.dreampany.common.misc.func.Executors
import com.dreampany.common.ui.model.UiTask
import timber.log.Timber
import kotlin.reflect.KClass

/**
 * Created by roman on 7/11/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
val Activity?.context: Context? get() = this

fun Activity?.alive(): Boolean {
    if (this == null) return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        return !(isFinishing() || isDestroyed())
    return !isFinishing()
}

fun Activity?.open(action: String, requestCode: Int) {
    this?.run {
        val intent = Intent(action)
        startActivityForResult(intent, requestCode)
    }
}

fun <T : Any> Activity?.open(target: KClass<T>, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java))
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(
    target: KClass<T>,
    task: Task<*, *, *, *, *>,
    finish: Boolean = false
) {
    this?.run {
        val intent = Intent(this, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivity(intent)
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(target: KClass<T>, task: Task<*, *, *, *, *>, requestCode: Int) {
    this?.run {
        val intent = Intent(this, target.java)
        intent.putExtra(Constant.Keys.TASK, task as Parcelable)
        startActivityForResult(intent, requestCode)
    }
}

fun <T : Activity> Activity?.open(target: KClass<T>, flags: Int, finish: Boolean = false) {
    this?.run {
        startActivity(Intent(this, target.java).addFlags(flags))
        if (finish) {
            finish()
        }
    }
}

fun <T : Any> Activity?.open(
    target: KClass<T>,
    finishCurrent: Boolean = false,
    clearTask: Boolean = false
) {
    this?.run {
        if (clearTask) {
            val intent = Intent(this, target.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            startActivity(Intent(this, target.java))
            if (finishCurrent) {
                finish()
            }
        }

    }
}

fun Activity?.close(task: Task<*, *, *, *, *>? = null) {
    this?.run {
        val intent = Intent()
        task?.let { intent.putExtra(Constant.Keys.TASK, it as Parcelable) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

val Activity?.clearFlags: Int
    get() = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

fun <T : Fragment> AppCompatActivity?.fragment(tag: String?): T? =
    this?.supportFragmentManager?.findFragmentByTag(tag) as T?

fun <T : Fragment> AppCompatActivity?.open(fragment: T?, @IdRes parent: Int, ex: Executors) {
    val runner = kotlinx.coroutines.Runnable {
        this?.run {
            if (isDestroyed || isFinishing) return@Runnable
            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(parent, it, it.javaClass.simpleName)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
    }
    ex.postToUi(runner)
}

val Intent?.bundle: Bundle? get() = this?.extras

val Activity?.bundle: Bundle? get() = this?.intent?.bundle

val Bundle?.task: Task<*, *, *, *, *>?
    get() = this?.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?

val Intent?.task: Task<*, *, *, *, *>?
    get() = this.bundle.task

val Activity?.task: Task<*, *, *, *, *>?
    get() = this.bundle?.getParcelable<Parcelable>(Constant.Keys.TASK) as Task<*, *, *, *, *>?

fun Activity?.finish(okay: Boolean) {
    this?.finish(this.task, okay)
}

fun <I : BaseParcel> Activity?.finish(
    input: I,
    okay: Boolean
) {
    val task = task as? UiTask<BaseEnum, BaseEnum, BaseEnum, BaseEnum, I>
    task?.input = input
    finish(task, okay)
}

fun Activity?.finish(task: Task<*, *, *, *, *>?, okay: Boolean = true) {
    if (this.alive().not()) return
    val intent = Intent()
    intent.putExtra(Constant.Keys.TASK, task as Parcelable?)
    if (okay) {
        this?.setResult(Activity.RESULT_OK, intent)
    } else {
        this?.setResult(Activity.RESULT_CANCELED, intent)
    }
    this?.finish()
}

fun Activity?.moreApps(devId: String) {
    if (this == null) return
    try {
        val intent = Intent()
        val uri = Uri.parse("market://search?q=pub:$devId")
        val market = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(market)
    } catch (error: ActivityNotFoundException) {
        Timber.e(error)
    }
}

fun Activity?.rateUs() {
    if (this == null) return
    rateUs(this.packageName)
}

fun Activity?.rateUs(packageName: String?) {
    if (this == null || packageName == null) return
    try {
        val id: String = packageName
        val uri = Uri.parse("market://details?id=$id")
        val market = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(market)
    } catch (error: ActivityNotFoundException) {
        Timber.e(error)
    }
}

fun Activity?.showKeyboard() {
    val view = this?.currentFocus ?: return
    kotlinx.coroutines.Runnable {
        val imm: InputMethodManager = (this.getSystemService(Context.INPUT_METHOD_SERVICE)
            ?: return@Runnable) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }.run()
}

fun Activity?.hideKeyboard() {
    val view = this?.currentFocus ?: return
    kotlinx.coroutines.Runnable {
        val imm: InputMethodManager = (this.getSystemService(Context.INPUT_METHOD_SERVICE)
            ?: return@Runnable) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }.run()
}

fun Activity?.openAppSettings(finishCurrent: Boolean = false) {
    this?.run {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }
}

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

