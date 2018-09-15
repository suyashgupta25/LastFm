package de.appsfactory.lastfm

import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import de.appsfactory.lastfm.di.AppModule
import de.appsfactory.lastfm.di.DaggerAppComponent
import de.appsfactory.lastfm.di.NetworkModule

open class BaseApp : DaggerApplication() {

    lateinit var androidInjector: AndroidInjector<out DaggerApplication>

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        androidInjector = DaggerAppComponent.builder()
                .application(this)
                .network(networkModule())
                .appmodule(appModule())
                .build()
    }

    public override fun applicationInjector(): AndroidInjector<out DaggerApplication> = androidInjector

    protected open fun networkModule(): NetworkModule = NetworkModule()

    protected open fun appModule(): AppModule = AppModule()

}