package com.dreampany.hi.misc.constant


/**
 * Created by roman on 5/8/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Constants {

    object Apis {
        const val GOOGLE_CLIENT_ID_DREAMPANY_MAIL =
            "387180098728-3ugp904a274k90p0a0vrb823t1v3ufqi.apps.googleusercontent.com" //django
    }

    object Keys {
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0

        object Pref {
            const val DEFAULT = "default"
            const val MISC = "misc"
            const val PREF = "pref"
            const val EXPIRE = "expire"
            const val STARTED = "started"
            const val LOGGED = "logged"
            const val SIGNED = "signed"
            const val AUTH = "auth"
            const val USER = "user"
        }

        object Room {
            const val MISC = "misc"
            const val ROOM = "room"
        }

        const val THUMB_URI = "thumb_uri"
        const val MIME_TYPE = "mime_type"
        const val DISPLAY_NAME = "display_name"
        const val FILE_ID = "file_id"
        const val PREFIX_FILE = "file_"
    }

    object Datetime {
        const val MESSAGE_PATTERN = "dd MMM yyyy hh:mm a"
    }
}