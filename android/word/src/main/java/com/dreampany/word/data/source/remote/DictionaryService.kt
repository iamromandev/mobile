package com.dreampany.word.data.source.remote

import com.dreampany.word.data.source.remote.model.WordObject
import com.dreampany.word.misc.constant.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by roman on 10/4/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
interface DictionaryService {
    @GET(Constants.Values.DictionaryService.WORDS)
    suspend fun getWord(
        @Path(Constants.Keys.DictionaryService.WORD)
        word : String
    ) : Response<WordObject>
}
