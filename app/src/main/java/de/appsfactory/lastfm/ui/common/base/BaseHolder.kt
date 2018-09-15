package de.appsfactory.lastfm.ui.common.base

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)

}