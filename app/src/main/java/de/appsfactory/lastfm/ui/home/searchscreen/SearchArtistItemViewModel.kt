package de.appsfactory.lastfm.ui.home.searchscreen

import android.databinding.ObservableField
import de.appsfactory.lastfm.data.model.Artist
import de.appsfactory.lastfm.data.model.ImageURL
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY

class SearchArtistItemViewModel(artist: Artist?) {

    val name = ObservableField<String>(EMPTY)
    val listenersNumber = ObservableField<String>(EMPTY)
    val url = ObservableField<String>(EMPTY)

    init {
        name.set(artist?.name)
        listenersNumber.set(artist?.listeners.toString())
        url.set(artist?.getImageURL(ImageURL.Size.MEDIUM)?.url)
    }
}