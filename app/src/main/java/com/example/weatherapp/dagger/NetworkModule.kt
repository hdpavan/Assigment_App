package com.example.weatherapp.dagger

import com.example.weatherapp.api.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {


    @Provides
    @Reusable
    @JvmStatic
    internal fun providePostApi(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {

        var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

}