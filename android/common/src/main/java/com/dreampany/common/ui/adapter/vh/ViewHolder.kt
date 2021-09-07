package com.dreampany.common.ui.adapter.vh

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by roman on 8/24/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class ViewHolder<in T>(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    protected val context: Context get() = itemView.context
    abstract fun bind(input: T)
}