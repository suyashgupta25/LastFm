package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class MyAlbumItemViewModel :ViewModel() {

    val imageUrl = ObservableField<String>("")
    val albumName = ObservableField<String>("")
}