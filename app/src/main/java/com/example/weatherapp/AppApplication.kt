package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.dagger.AppComponent
import com.example.weatherapp.dagger.AppModule
import com.example.weatherapp.dagger.DaggerAppComponent
import com.example.weatherapp.dagger.NetworkModule


class AppApplication : Application() {

    lateinit var networkComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        networkComponent = DaggerAppComponent
            .builder()
            .networkModule(NetworkModule)
            .appModule(AppModule(this))
            .build()
    }

}