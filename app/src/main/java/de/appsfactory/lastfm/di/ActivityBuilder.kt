package de.appsfactory.lastfm.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.appsfactory.lastfm.ui.home.HomeActivity

@Module
internal abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeHomeInjector(): HomeActivity

}