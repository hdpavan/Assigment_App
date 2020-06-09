package com.example.weatherapp.utility

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import javax.inject.Inject
import javax.inject.Singleton

class PreferenceManager @Inject constructor(context: Context) {

    var mPrefs: SharedPreferences? = null

    init {
        mPrefs = context.getSharedPreferences("pawan", 0)
    }

    companion object {
        const val LATITUDE: String = "LATITUDE"
        const val LONGITUDE: String = "LONGITUDE"

       /* fun newInstance(tutorial: PreferenceManager): PreferenceManager {
            val fragmentHome = PreferenceManager()

            return fragmentHome
        }*/

    }

    fun getLatitude(): Float {
        return mPrefs!!.getFloat(LATITUDE, 0f)
    }

    fun setLatitude(value: Float) {
        mPrefs!!.edit().putFloat(LATITUDE, value).apply()
    }


    fun getLongitude(): Float {
        return mPrefs!!.getFloat(LONGITUDE, 0f)
    }

    fun setLongitude(value: Float) {
        mPrefs!!.edit().putFloat(LONGITUDE, value).apply()

    }


}