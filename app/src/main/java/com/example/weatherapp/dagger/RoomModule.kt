package com.example.weatherapp.dagger

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.model.persistence.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "WorkManager.db").build()

    }

}