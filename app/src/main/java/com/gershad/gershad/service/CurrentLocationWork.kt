package com.gershad.gershad.service

import android.Manifest
import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
//import com.crashlytics.android.Crashlytics
import com.gershad.gershad.BaseApplication
import com.gershad.gershad.Constants.Companion.PRIMARY_CHANNEL
import com.gershad.gershad.R
import com.gershad.gershad.dependency.GershadPreferences
import com.gershad.gershad.dependency.Module
import com.gershad.gershad.map.MapActivity
import com.gershad.gershad.map.MapFragment.Companion.LATITUDE
import com.gershad.gershad.map.MapFragment.Companion.LONGITUDE
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import space.traversal.kapsule.Injects
import space.traversal.kapsule.inject
import space.traversal.kapsule.required


open class CurrentLocationWork(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters), Injects<Module> {


    private val gershadSettings: GershadPreferences by required { preferences }
    private val repository by required { repository }


    override fun doWork(): Result {
        inject(BaseApplication.module(applicationContext))

        if (gershadSettings.reportsNearYou) {
            try {
                val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener {
                        if (it.isSuccessful && it.result != null) {
                            var location = it.result
                            if (location is Location) {
                                repository.getReports(LatLng(location.latitude, location.longitude))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ response ->
                                            if (response.isNotEmpty()) {
                                                var notify = false
                                                response.forEach {
                                                    if (it.fadeValue == 1.0F) {
                                                        notify = true
                                                    }
                                                }
                                                if (notify) {
                                                    sendNotification(it.hashCode(), applicationContext.getString(R.string.app_name), applicationContext.getString(R.string.reports_near_you), location.latitude, location.longitude)
                                                }
                                            }
                                        }, { _ ->
                            })
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().recordException(ex)
                //Crashlytics.logException(ex)
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun sendNotification(id: Int, title: String, messageBody: String, latitude: Double, longitude: Double) {
        val intent = Intent(applicationContext, MapActivity::class.java)
        intent.putExtra(LATITUDE, latitude)
        intent.putExtra(LONGITUDE, longitude)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(applicationContext, id, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(id, notificationBuilder.build())
    }
}