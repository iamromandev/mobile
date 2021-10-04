package com.dreampany.word.misc.constant

/**
 * Created by roman on 10/1/21
 * Copyright (c) 2021 epany. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Constants {
    object Keys {
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
            const val LANGUAGE_ID = "language_id"
        }
        object DictionaryService {
            const val WORD = "word"
        }
    }

    object Values {
        object Room {
            const val ROOM = "room"
        }
        object DictionaryService {
            const val BASE_URL = "http://api.epany.io/"

            const val WORDS = "dictionary/words/{word}"
        }
    }
}