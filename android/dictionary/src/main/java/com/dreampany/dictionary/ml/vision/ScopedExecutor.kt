package com.dreampany.dictionary.ml.vision

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by roman on 10/8/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class ScopedExecutor(private val executor: Executor) : Executor {

    private val shutdown = AtomicBoolean()

    override fun execute(command: Runnable) {
        if (shutdown.get()) return
        executor.execute {
            if (shutdown.get()) return@execute
            command.run()
        }
    }

    fun shutdown() = shutdown.set(true)
}
