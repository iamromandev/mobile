package com.dreampany.tube.ui.vm

import android.app.Application
import android.location.Location
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.model.Library
import com.dreampany.tube.data.model.Video
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.data.source.repo.CategoryRepo
import com.dreampany.tube.data.source.repo.VideoRepo
import com.dreampany.tube.misc.Constants
import com.dreampany.tube.ui.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 1/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class VideoViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper,
    private val pref: Prefs,
    private val categoryRepo: CategoryRepo,
    private val repo: VideoRepo
) : BaseViewModel<Type, Subtype, State, Action, Video, VideoItem, UiTask<Type, Subtype, State, Action, Video>>(
    application,
    rm
) {

    fun loadLocalVideos(countryCode: String, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result =
                    repo.getsOfRegionCode(countryCode, order, offset, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadLocationVideos(location: Location, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                val loc = "${location.latitude},${location.longitude}"
                val radius = "10mi"
                result = repo.getsOfLocation(loc, radius, order, offset, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadEventVideos(eventType: String, order: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfEvent(eventType, order, offset, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadVideos(categoryId: String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfCategoryId(categoryId, offset, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadSearch(query: String, order: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfQuery(query, order, 0, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadRelated(id: String, order: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                result = repo.getsOfRelated(id, order, 0, Constants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun readLibraries(libraryType: String) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {
                when (libraryType) {
                    Library.Type.RECENT.name -> {
                        result = repo.recents()
                    }
                    Library.Type.FAVORITE.name -> {
                        result = repo.favorites()
                    }
                }
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }

    fun loadVideo(input: Video) {
        uiScope.launch {
            //postProgressSingle(true)
            var result: Video? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.isFavorite(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = State.DEFAULT)
            }
        }
    }

    fun toggleFavorite(input: Video) {
        uiScope.launch {
            var result: Video? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.toggleFavorite(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = State.FAVORITE)
            }
        }
    }

    fun writeRecent(input: Video) {
        uiScope.launch {
            var result: Video? = null
            var errors: SmartError? = null
            var favorite: Boolean = false
            try {
                favorite = repo.isFavorite(input)
                repo.writeRecent(input)
                result = input
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItem(favorite), state = State.RECENT)
            }
        }
    }

    /*fun loadVideosOfRegionCode(regionCode : String, offset: Long) {
        uiScope.launch {
            postProgressMultiple(true)
            var result: List<Video>? = null
            var errors: SmartError? = null
            try {

                result = repo.getsOfRegionCode(regionCode, offset, AppConstants.Limits.VIDEOS)
            } catch (error: SmartError) {
                Timber.e(error)
                errors = error
            }
            if (errors != null) {
                postError(errors)
            } else {
                postResult(result?.toItems())
            }
        }
    }*/

    private suspend fun List<Video>.toItems(): List<VideoItem> {
        val input = this
        return withContext(Dispatchers.IO) {
            input.map { input ->
                val favorite = repo.isFavorite(input)
                VideoItem(input, favorite)
            }
        }
    }

    private suspend fun Video.toItem(favorite: Boolean): VideoItem {
        val input = this
        return withContext(Dispatchers.IO) {
            VideoItem(input, favorite)
        }
    }

    private fun postProgressSingle(progress: Boolean) {
        postProgressSingle(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }

    private fun postProgressMultiple(progress: Boolean) {
        postProgressMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            progress = progress
        )
    }


    private fun postError(error: SmartError) {
        postMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            error = error,
            showProgress = true
        )
    }

    private fun postResult(result: List<VideoItem>?) {
        postMultiple(
            Type.VIDEO,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }

    private fun postResult(result: VideoItem?, state: State = State.DEFAULT) {
        postSingle(
            Type.VIDEO,
            Subtype.DEFAULT,
            state,
            Action.DEFAULT,
            result = result,
            showProgress = true
        )
    }
}