package com.example.weatherapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Repository
import com.example.weatherapp.model.JSONResponse
import com.example.weatherapp.model.persistence.Database
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    var jsonResponse = MutableLiveData<JSONResponse>()

    var isLoading = MutableLiveData<Boolean>()


    fun fetchFromData() {

        isLoading.postValue(true)

        viewModelScope.launch {

            // val data = repository.getALL()

            val dt = async {
                return@async repository.getALL()
            }.await()

            if (!dt?.isEmpty()) {
                val model: JSONResponse = Gson().fromJson(
                    dt?.get(0)!!.data,
                    JSONResponse::class.java
                )
                isLoading.postValue(false)
                jsonResponse.value = model
            } else {

                Log.d("MainActivityModel", "Empty")
            }

        }
    }
}