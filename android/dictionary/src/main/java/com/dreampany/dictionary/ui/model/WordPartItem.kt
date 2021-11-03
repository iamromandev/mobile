package com.dreampany.dictionary.ui.model

import androidx.databinding.ViewDataBinding
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 10/19/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class WordPartItem<T, B>
constructor(
    open var input: T
) : ModelAbstractBindingItem<T, B>(input) where B : ViewDataBinding