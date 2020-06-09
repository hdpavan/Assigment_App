package com.example.weatherapp.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.AppApplication
import com.example.weatherapp.R
import com.example.weatherapp.api.Repository
import javax.inject.Inject

/**
 * EXAMPLE WORKER to do a task in background
 */
class TrackLocationWorker @Inject constructor(val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    @Inject
    lateinit var repository: Repository


    companion object {
        val TAG = TrackLocationWorker::class.java.simpleName
        val FIRST_KEY = "ARG_EXTRA_PARAM"
        val SECOND_KEY = "OUTPUT_DATA_PARAM1"

    }

    init {
        (context as AppApplication).networkComponent.inject(this)
    }


    override fun doWork(): Result {

        repository.locationRepository.getLocation()

        return Result.success()
    }


    fun sendNotification(title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1, notification.build())
    }


}