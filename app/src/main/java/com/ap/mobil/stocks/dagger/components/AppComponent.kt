package com.ap.mobil.stocks.dagger.components

import android.app.Application
import com.ap.mobil.stocks.App
import com.ap.mobil.stocks.dagger.modules.ActivityModule
import com.ap.mobil.stocks.dagger.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/2/18.
 *
 * This is the main injection class that injects [com.ap.mobil.stocks.App] Activities, and android modules.
 */
@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (ActivityModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: App)
}