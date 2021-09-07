package com.dreampany.common.ui.misc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by roman on 7/17/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class ItemSpaceDecoration(
    var horizontalSpaceWidth: Int = 0,
    var verticalSpaceHeight: Int = 0,
    var spanCount: Int = 1,
    var includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column: Int = position % spanCount

        if (includeEdge) {
            outRect.right = horizontalSpaceWidth - column * horizontalSpaceWidth / spanCount
            outRect.left = (column + 1) * horizontalSpaceWidth / spanCount

            if (position < spanCount) {
                outRect.top = verticalSpaceHeight
            }
            outRect.bottom = verticalSpaceHeight
            return
        }
        outRect.right = column * horizontalSpaceWidth / spanCount
        outRect.left = horizontalSpaceWidth - (column + 1) * horizontalSpaceWidth / spanCount
        if (position >= spanCount) {
            outRect.top = verticalSpaceHeight
        }
    }
}