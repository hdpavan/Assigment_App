package com.example.weatherapp.api

import com.example.weatherapp.model.persistence.Database
import com.example.weatherapp.model.persistence.entity.WeatherInfo
import javax.inject.Inject

class Repository @Inject constructor(
    val locationRepository: LocationRepository,
    private val database: Database
) {


    suspend fun getALL(): List<WeatherInfo> {

        return database.locationDao().selectAll()

    }


}