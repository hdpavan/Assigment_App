package com.example.weatherapp.api

import com.example.weatherapp.model.JSONResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun fetchDataByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ): Response<JSONResponse>


}