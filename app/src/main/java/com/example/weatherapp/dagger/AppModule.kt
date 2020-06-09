package com.example.weatherapp.dagger

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationRequest
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {


    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }

    @Module
    companion object {


        @Provides
        @Singleton
        @JvmStatic
        fun locationRequest(): LocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 3 * 1000
            fastestInterval = 5 * 1000
        }


    }
}