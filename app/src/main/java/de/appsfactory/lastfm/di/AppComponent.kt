package de.appsfactory.lastfm.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.appsfactory.lastfm.BaseApp
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilder::class,
        AppModule::class
        ])
interface AppComponent : AndroidInjector<BaseApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: BaseApp): Builder

        fun network(network: NetworkModule): Builder

        fun appmodule(appmodule: AppModule): Builder

        fun build(): AppComponent
    }

    override fun inject(app: BaseApp)

}