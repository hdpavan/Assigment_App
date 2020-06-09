package com.example.weatherapp.model.persistence.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeatherInfo")
data class WeatherInfo(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,

    @ColumnInfo(name = "response") val data: String

)