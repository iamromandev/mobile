package com.dreampany.tube.api.model

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoSnippet(
    val categoryId: String,
    val title: String,
    val description: String,
    val channelId: String,
    val channelTitle: String,
    val thumbnails : Map<String, Thumbnail>,
    val tags : List<String>,
    val liveBroadcastContent: String,
    val publishedAt: String
)