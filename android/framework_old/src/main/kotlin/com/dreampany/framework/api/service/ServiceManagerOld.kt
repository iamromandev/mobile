/*
package com.dreampany.frame.api.service

import android.content.Context
import android.content.Intent
import com.dreampany.frame.util.AndroidUtil
import com.firebase.jobdispatcher.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


*/
/**
 * Created by Hawladar Roman on 7/23/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*

@Singleton
class ServiceManager @Inject constructor(val context: Context) {
    val dispatcher: FirebaseJobDispatcher

    init {
        dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
    }

    fun <T : BaseService> openService(classOfT: Class<T>) {
        val intent = Intent(context, classOfT)
        openService(intent)
    }

    fun openService(intent: Intent) {
        if (AndroidUtil.hasOreo()) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun <T : BaseJobService> scheduleService(classOfT: Class<T>, period: Int) {
        val tag = classOfT.simpleName
        //dispatcher.cancel(tag)
        val job = dispatcher.newJobBuilder()
            .setService(classOfT)
            .setTag(tag)
            .setLifetime(Lifetime.FOREVER)
            .setTrigger(Trigger.executionWindow(period - 1, period))
            .setReplaceCurrent(true)
            .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
            .setConstraints(Constraint.ON_ANY_NETWORK, Constraint.DEVICE_IDLE)
            .build()
        try {
            dispatcher.mustSchedule(job)
        } catch (error: FirebaseJobDispatcher.ScheduleFailedException) {
            Timber.e(tag, "error: %s", error.toString())
        }

    }

    fun <T : BaseJobService> schedulePowerService(classOfT: Class<T>, period: Int) {
        val tag = classOfT.simpleName
        //dispatcher.cancel(tag)
        val job = dispatcher.newJobBuilder()
            .setService(classOfT)
            .setTag(tag)
            .setLifetime(Lifetime.FOREVER)
            .setTrigger(Trigger.executionWindow(period - 1, period))
            .setReplaceCurrent(true)
            .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
            .setConstraints(Constraint.ON_ANY_NETWORK)
            .build()
        try {
            dispatcher.mustSchedule(job)
        } catch (error: FirebaseJobDispatcher.ScheduleFailedException) {
            Timber.e(tag, "error: %s", error.toString())
        }
    }

    fun <T : BaseJobService> cancel(classOfT: Class<T>) {
        val tag = classOfT.simpleName
        dispatcher.cancel(tag)
    }


}*/
