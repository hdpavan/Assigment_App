package com.example.weatherapp.model.persistence.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.model.persistence.entity.WeatherInfo

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherInfo: WeatherInfo): Long

    @Query("SELECT * FROM WeatherInfo")
    suspend fun selectAll(): List<WeatherInfo>


}
