package de.appsfactory.lastfm.utils

import android.util.Log

/**
 * Created by suyashg on 21/07/18.
 *
 * If we want to dump error somewhere locally this is the place for it
 */
fun defaultErrorHandler(): (Throwable) -> Unit = { e -> Log.e("Error", e.message, e) }