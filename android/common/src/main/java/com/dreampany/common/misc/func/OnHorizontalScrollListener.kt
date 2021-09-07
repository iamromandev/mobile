package com.dreampany.common.misc.func

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by roman on 8/28/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class OnHorizontalScrollListener(var scrollingCallAtEnd: Boolean = false) :
    RecyclerView.OnScrollListener() {

    private val delay = 3000
    var scrolling = false
        get() = field
        private set(value) { field = value }
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val runner = Runnable { onScrollingAtEnd() }

    override fun onScrollStateChanged(recycler: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrolling = false
            onIdle()
        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            scrolling = true
            onScrolling()
            if (scrollingCallAtEnd) {
                handler.removeCallbacks(runner)
                handler.postDelayed(runner, delay.toLong())
            }
        }
    }

    override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
        if (!recycler.canScrollHorizontally(-1)) {
            onScrolledToTop()
        } else if (!recycler.canScrollHorizontally(1)) {
            onScrolledToBottom()
        }
        if (dx < 0) {
            onScrolledUp(dx)
        } else if (dx > 0) {
            onScrolledDown(dx)
        }
    }

    private fun onScrolledUp(dx: Int) {
        onScrolledUp()
    }

    private fun onScrolledDown(dx: Int) {
        onScrolledDown()
    }

    open fun onIdle() {}

    open fun onScrolling() {}

    open fun onScrollingAtEnd() {}

    open fun onScrolledUp() {}

    open fun onScrolledDown() {}

    open fun onScrolledToTop() {}

    open fun onScrolledToBottom() {}
}