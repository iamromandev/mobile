package com.dreampany.word.data.source.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 10/11/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class DefinitionObject(
    val id: String,
    val source: String,
    @SerializedName("part_of_speech")
    val partOfSpeech: String,
    val definition: String
)