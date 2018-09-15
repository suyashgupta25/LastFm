package de.appsfactory.lastfm.di

import dagger.Module
import dagger.Provides
import de.appsfactory.lastfm.BaseApp
import de.appsfactory.lastfm.data.albums.AlbumDataSource
import de.appsfactory.lastfm.data.albums.LastFmService
import de.appsfactory.lastfm.data.albums.AlbumSource
import de.appsfactory.lastfm.data.albums.local.AlbumDao
import de.appsfactory.lastfm.data.albums.local.AppDatabase
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ViewModelModule::class])
open class AppModule {

    @Provides
    @Singleton
    open fun buildDatabase(app: BaseApp): AppDatabase =
            AppDatabase.getInstance(app.applicationContext)

    @Provides
    @Singleton
    open fun getAlbumDao(appDatabase: AppDatabase): AlbumDao =
            appDatabase.albumDao()

    @Provides
    @Singleton
    open fun getAlbumSource(albumDao: AlbumDao, lastFmService: LastFmService): AlbumSource =
            AlbumDataSource(albumDao, lastFmService)
}