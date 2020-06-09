package com.example.weatherapp

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.weatherapp.WorkManager.TrackLocationWorker
import com.example.weatherapp.utility.*
import com.example.weatherapp.viewmodel.MainActivityViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.location_error.*
import kotlinx.android.synthetic.main.location_info.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferenceManager: PreferenceManager

    val mViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(broadCastReceiver, getIntentFilter())

        showLocationPrompt()

        (application as AppApplication).networkComponent.inject(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mViewModel.fetchFromData()

        launchSettings.setOnClickListener({
            launchAppSettings()
        })

        mViewModel.jsonResponse.observe(this, Observer { jsonsResponse ->

            loc_info.visibility = View.VISIBLE
            loc_err.visibility = View.GONE

            displayData(jsonsResponse)

            Glide.with(this)
                .load(getImageUrl(jsonsResponse.weather?.get(0)?.icon))
                .into(imageView2)
        })

        mViewModel.isLoading.observe(this, Observer {

            if (it)
                progress.visibility = View.VISIBLE
            else
                progress.visibility = View.GONE


        })

        initPeriodicWorker()
    }


    private fun initPeriodicWorker() {

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicBuilder =
            PeriodicWorkRequest.Builder(TrackLocationWorker::class.java, 2, TimeUnit.HOURS)

        val myWork = periodicBuilder.addTag(TrackLocationWorker.TAG)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TrackLocationWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            when (intent?.action) {

                "com.example.Broadcast" -> {

                    val locStatus = intent.getBooleanExtra("LocResult", false)

                    if (locStatus) {
                        Log.d(TAG, "Update 01")
                        mViewModel.fetchFromData()
                    } else {
                        getLastLocation()
                    }

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getLastLocation()

    }

    private fun getIntentFilter(): IntentFilter {
        val iFilter = IntentFilter()
        iFilter.addAction("com.example.Broadcast")
        return iFilter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver)

    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            Log.d(
                TAG,
                "Location" + mLastLocation.latitude.toString() + "ln" + mLastLocation.longitude.toString()
            )
            preferenceManager.setLatitude(mLastLocation.latitude.toFloat())
            preferenceManager.setLongitude(mLastLocation.longitude.toFloat())

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //when granted
                getLastLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationRequest.PRIORITY_HIGH_ACCURACY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("Status: ", "On")
                } else {
                    Log.e("Status: ", "Off")
                }
            }
        }
    }


}
