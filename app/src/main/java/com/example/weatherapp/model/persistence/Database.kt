package com.example.weatherapp.model.persistence


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.model.persistence.Dao.WeatherDao
import com.example.weatherapp.model.persistence.entity.WeatherInfo


@Database(entities = [WeatherInfo::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun locationDao(): WeatherDao

}
