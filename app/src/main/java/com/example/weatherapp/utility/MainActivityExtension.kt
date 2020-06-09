package com.example.weatherapp.utility

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.MainActivity
import com.example.weatherapp.model.JSONResponse
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.location_info.*

@SuppressLint("MissingPermission")
fun MainActivity.getLastLocation() {
    if (checkPermissions()) {
        if (isLocationEnabled()) {

            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    Log.d(
                        TAG,
                        "LocationUpdate " + location.latitude.toString() + "ln" + location.longitude.toString()
                    )

                    initailLanunch(location.latitude.toFloat(), location.longitude.toFloat())

                    preferenceManager.setLatitude(location.latitude.toFloat())
                    preferenceManager.setLongitude(location.longitude.toFloat())

                }
            }
        } else {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    } else {
        requestPermissions()
    }
}

fun MainActivity.initailLanunch(lat: Float, lng: Float) {


    if (preferenceManager.getLongitude() == 0f && preferenceManager.getLatitude() == 0f) {
        //initialy
        mViewModel.repository.locationRepository.getLocation()
    }


}

@SuppressLint("MissingPermission")
fun MainActivity.requestNewLocationData() {
    var mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 0
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    mFusedLocationClient!!.requestLocationUpdates(
        mLocationRequest, mLocationCallback,
        Looper.myLooper()
    )
}


fun MainActivity.displayData(jsonResponse: JSONResponse) {

    val data = "Name:  " + jsonResponse.name + "\n" +
            "Latitude:  " + jsonResponse.Coord?.lat + "\n" +
            "Longitude:  " + jsonResponse.Coord?.lon + "\n" +
            "Latitude:  " + jsonResponse.Coord?.lat + "\n" +
            "Description: " + jsonResponse.weather?.get(0)?.main + " , " + jsonResponse.weather?.get(
        0
    )?.description + "\n" +
            "Temp:  " + jsonResponse.main?.temp + "\n" +
            "Temp Min:  " + jsonResponse.main?.temp_min + "\n" +
            "Temp Max:  " + jsonResponse.main?.temp_max + "\n" +
            "Humidity:  " + jsonResponse.main?.humidity + "\n" +
            "Pressure:  " + jsonResponse.main?.pressure + "\n"


    tv.text = data


}

fun MainActivity.isLocationEnabled(): Boolean {
    var locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

fun MainActivity.checkPermissions(): Boolean {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

fun MainActivity.requestPermissions() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        PERMISSION_ID
    )
}

fun MainActivity.launchAppSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    val uri =
        Uri.fromParts("package", getPackageName(), null)
    intent.data = uri
    startActivityForResult(intent, 909)

}

fun MainActivity.showLocationPrompt() {
    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val result: Task<LocationSettingsResponse> =
        LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

    result.addOnCompleteListener { task ->
        try {
            val response = task.getResult(ApiException::class.java)
            // All location settings are satisfied. The client can initialize location
            // requests here.
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        // Cast to a resolvable exception.
                        val resolvable: ResolvableApiException = exception as ResolvableApiException
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        resolvable.startResolutionForResult(
                            this, LocationRequest.PRIORITY_HIGH_ACCURACY
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    } catch (e: ClassCastException) {
                        // Ignore, should be an impossible error.
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Location settings are not satisfied. But could be fixed by showing the
                    // user a dialog.

                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                }
            }
        }
    }
}


fun MainActivity.getImageUrl(icon: String?): String {
    return "https://openweathermap.org/img/wn/" + icon + ".png"
}


