package com.dreampany.news.misc

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {

    object Keys {
        object Pref {
            const val PREF = "pref"
            const val MISC = "misc"
            const val EXPIRE = "expire"
            const val CATEGORY = "category"
            const val PAGE = "page"
            const val CATEGORIES = "categories"
            const val PAGES = "pages"
            const val SEARCH = "search"
            const val VIDEO = "video"
            const val VIDEOS = "videos"
        }

        object Room {
            const val MISC = "misc"
            const val ROOM = "room"
        }

        object Related {
            const val LEFTER = "lefter"
            const val RIGHTER = "righter"
        }

        const val SOURCE_ID = "source_id"
    }

    object Values {
        const val ARTICLES = "news.articles"
        const val SEARCH = "news.search"
    }

    object Limits {
        const val VIDEOS = 50L
    }

    object Times {
        val HOUSE_ADS = TimeUnit.DAYS.toMillis(1)
        val CATEGORIES = TimeUnit.DAYS.toMillis(7)
        val NEWS = TimeUnit.HOURS.toMillis(1)
    }

    object Count {
        const val MIN_PAGES = 3
    }
}