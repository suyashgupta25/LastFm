package de.appsfactory.lastfm.utils

class AppConstants {

    companion object {
        const val EMPTY = ""

        //Network settings
        const val DATABASE_NAME = "lastfm-db"

        //Network settings
        const val CONNECTION_TIMEOUT = 10L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 10L

        //Paged List adapter
        const val PAGE_SIZE = 16
        const val PPREFETCH_SIZE = 16
    }
}