package com.dreampany.framework.api.key

import com.dreampany.framework.util.NumberUtil
import org.apache.commons.collections4.queue.CircularFifoQueue
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class KeyManager
@Inject constructor() {
    private lateinit var queue: CircularFifoQueue<Int>
    private val keys = mutableListOf<String>()
    val length: Int
        get() = keys.size

    fun setKeys(vararg keys: String) {
        queue = CircularFifoQueue(keys.size)
        this.keys.clear()
        this.keys.addAll(keys)
        for (index in 0..this.keys.size - 1) {
            queue.add(index)
        }
    }

    fun forwardKey() {
        queue.add(queue.peek())
    }

    fun randomForwardKey() {
        val rand = NumberUtil.nextRand((length / 2) + 1)
        for (index in 1..rand) {
            queue.add(queue.peek())
        }
    }

    fun getKey(): String {
        return keys.get(queue.peek()!!)
    }
}