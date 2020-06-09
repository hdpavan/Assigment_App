package com.example.weatherapp.dagger

import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindviewmodelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}