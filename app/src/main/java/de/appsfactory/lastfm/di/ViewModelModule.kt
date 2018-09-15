package de.appsfactory.lastfm.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import de.appsfactory.lastfm.ui.home.albumdetails.AlbumDetailsViewModel
import de.appsfactory.lastfm.ui.home.myalbumsscreen.MyAlbumsViewModel
import de.appsfactory.lastfm.ui.home.searchscreen.SearchViewModel
import de.appsfactory.lastfm.ui.home.topalbums.TopAlbumsViewModel

/**
 * Created by suyashg
 */
@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MyAlbumsViewModel::class)
    fun bindMyAlbumsViewModel(viewModel: MyAlbumsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumDetailsViewModel::class)
    fun bindAlbumDetailsViewModel(viewModel: AlbumDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAlbumsViewModel::class)
    fun bindTopAlbumsViewModel(viewModel: TopAlbumsViewModel): ViewModel

}