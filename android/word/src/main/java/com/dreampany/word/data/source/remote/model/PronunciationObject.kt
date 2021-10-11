package com.dreampany.word.data.source.remote.model

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class PronunciationObject(
    val id: String,
    val source: String,
    val pronunciation: String,
    val url: String?
)