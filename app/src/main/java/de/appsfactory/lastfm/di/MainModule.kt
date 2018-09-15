package de.appsfactory.lastfm.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.appsfactory.lastfm.ui.home.albumdetails.AlbumDetailsFragment
import de.appsfactory.lastfm.ui.home.myalbumsscreen.MyAlbumsFragment
import de.appsfactory.lastfm.ui.home.searchscreen.SearchFragment
import de.appsfactory.lastfm.ui.home.topalbums.TopAlbumsFragment

@Module
internal abstract class MainModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMyAlbumsFragmentInjector(): MyAlbumsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeTopAlbumsFragmentInjector(): TopAlbumsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragmentInjector(): SearchFragment

    @ContributesAndroidInjector
    internal abstract fun contributeAlbumDetailsFragmentInjector(): AlbumDetailsFragment

}