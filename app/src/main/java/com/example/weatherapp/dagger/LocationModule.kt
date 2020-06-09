package com.example.weatherapp.dagger

import android.app.Application
import android.content.Context
import com.example.weatherapp.api.LocationRepository
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.model.persistence.Database
import com.example.weatherapp.utility.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideLocation(
        context: Context,
        apiService: WeatherApiService,
        database: Database,
        preferenceManager: PreferenceManager
    ): LocationRepository {

        return LocationRepository(context, apiService, database, preferenceManager)

    }


}