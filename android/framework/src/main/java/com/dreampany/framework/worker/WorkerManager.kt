package com.dreampany.framework.worker

import android.content.Context
import androidx.work.*
import com.dreampany.framework.misc.util.WorkerUtil
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WorkerManager
@Inject constructor(
    private val context: Context
) {
    @Inject
    internal lateinit var factory: WorkerFactory

    fun init() {
        WorkManager.initialize(context, Configuration.Builder().setWorkerFactory(factory).build())
    }

    fun <W : BaseWorker> createPeriodic(classOfWorker: KClass<W>, period: Long, unit: TimeUnit) {
        val workId = classOfWorker.java.canonicalName
        val work = WorkerUtil.createPeriodicWorkRequest(classOfWorker, Data.EMPTY, period, unit)
        val operation = WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(workId!!, ExistingPeriodicWorkPolicy.REPLACE, work)
        Timber.v(operation.result.toString())
    }

    fun <W : BaseWorker> cancel(classOfWorker: KClass<W>) {
        val workId = classOfWorker.java.canonicalName
        WorkManager.getInstance(context).cancelUniqueWork(workId!!)
    }
}