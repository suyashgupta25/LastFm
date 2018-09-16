package de.appsfactory.lastfm.ui.home.topalbums

import android.databinding.ObservableField
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.utils.AppConstants

class TopAlbumsItemViewModel (album: Album?) {

    val name = ObservableField<String>(AppConstants.EMPTY)
    val playcount = ObservableField<String>(AppConstants.EMPTY)
    val url = ObservableField<String>(AppConstants.EMPTY)

    init {
        name.set(album?.name)
        playcount.set(album?.playcount.toString())
        url.set(album?.getImageURL(ImageURL.Size.MEDIUM)?.url)
    }
}