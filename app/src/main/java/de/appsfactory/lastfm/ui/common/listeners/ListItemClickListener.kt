package de.appsfactory.lastfm.ui.common.listeners

import android.view.View

interface ListItemClickListener {
    fun onClick(view: View, position: Int)

    fun onRetryClick(position: Int)
}
