package com.dreampany.word.ml.vision

import android.app.ActivityManager
import android.content.Context
import com.google.android.gms.tasks.TaskExecutors
import java.util.*

/**
 * Created by roman on 10/7/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class VisionProcessor<T>(context: Context) : VisionImageProcessor {

    private var activityManager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val fpsTimer = Timer()
    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

    private var isShutdown: Boolean = false
    private var numRuns: Int = 0
    private var totalFrameMs: Long = 0L
    private var maxFrameMs = 0L
    private var minFrameMs = Long.MAX_VALUE
    private var totalDetectorMs = 0L
    private var maxDetectorMs = 0L
    private var minDetectorMs = Long.MAX_VALUE

    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

}