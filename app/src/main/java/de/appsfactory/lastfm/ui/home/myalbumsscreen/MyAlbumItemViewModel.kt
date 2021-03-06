package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.databinding.ObservableField
import de.appsfactory.lastfm.data.model.Album
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.utils.AppConstants

class MyAlbumItemViewModel(album: Album?) {

    val name = ObservableField<String>(AppConstants.EMPTY)
    val artistName = ObservableField<String>(AppConstants.EMPTY)
    val url = ObservableField<String>(AppConstants.EMPTY)

    init {
        name.set(album?.name)
        artistName.set(album?.artist?.name)
        url.set(album?.getImageURL(ImageURL.Size.LARGE)?.url)
    }
}