package com.dreampany.word.data.source.remote.model

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
data class WordObject(
    val id: String,
    val word: String,
    val origin: String?,
    val language: LanguageObject,
    val pronunciations: MutableList<PronunciationObject>? = null,
    val definitions: MutableList<DefinitionObject>? = null,
    val examples: MutableList<ExampleObject>? = null,
    val relations: MutableMap<String, MutableMap<String, String>>? = null
)