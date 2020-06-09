package com.example.weatherapp.dagger

import com.example.weatherapp.MainActivity
import com.example.weatherapp.WorkManager.TrackLocationWorker
import dagger.Component
import javax.inject.Singleton

@Singleton

@Component(
    modules =
    [NetworkModule::class,
        ViewModelModule::class,
        AppModule::class,
        LocationModule::class,
        RoomModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(worker: TrackLocationWorker)

}