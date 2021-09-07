package com.dreampany.hi.ui.model

import androidx.databinding.ViewDataBinding
import com.dreampany.hi.data.model.Message
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem

/**
 * Created by roman on 8/25/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class MessageItem<B>(open var input: Message) :
    ModelAbstractBindingItem<Message, B>(input) where B : ViewDataBinding