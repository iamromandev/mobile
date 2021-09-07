package com.dreampany.framework.misc.exts

import android.database.Cursor
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dreampany.framework.misc.constant.Constant
import com.google.common.hash.Hashing
import kotlinx.coroutines.Runnable
import java.util.*

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
fun Runnable.isOnUiThread(): Boolean =
    Thread.currentThread() === Looper.getMainLooper().getThread()

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

val hash256: Long
    get() = UUID.randomUUID().toString().hash256

val ByteArray?.hash256: Long
    get() {
        if (this == null || isEmpty) return 0L
        return Hashing.sha256().newHasher().putBytes(this).hash().asLong()
    }

val String?.hash256: Long
    get() = this?.toByteArray().hash256

val Cursor?.has: Boolean
    get() {
        if (this == null) return false
        if (isClosed) return false
        if (count <= 0) {
            close()
            return true
        }
        return false
    }

val Cursor?.close: Boolean
    get() {
        if (this != null && !isClosed) {
            this.close()
            return true
        }
        return false
    }

val String.id: String
    get() = this.replace(Constant.Regex.WHITE_SPACE, Constant.Default.STRING)
