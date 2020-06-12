package com.kleverowl.staffapp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.activities.SplashActivity
import com.kleverowl.staffapp.utils.AppConstants.CHANNEL_ID
import com.kleverowl.staffapp.utils.PreferenceConnector
import com.kleverowl.staffapp.utils.StaffType

class StaffFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        println("########## Triggered ###########")


        val type = PreferenceConnector.readInteger(this, PreferenceConnector.TYPE, StaffType.STAFF)

        if (type == StaffType.STAFF) {

            val map = p0.data
            var title: String = ""
            var description: String = ""
            for (d in map) {
                if (d.key == "title") {
                    title = d.value
                }
                if (d.key == "description") {
                    description = d.value
                }
            }

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = CHANNEL_ID
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
                )
                mNotificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelId)
            }

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }

        } else {

            val map = p0.data
            println("########### $map")
            var title: String = ""
            var description: String = ""
            for (d in map) {
                if (d.key == "title") {
                    title = d.value
                }
                if (d.key == "description") {
                    description = d.value
                }
            }

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = CHANNEL_ID
                val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
                )
                mNotificationManager.createNotificationChannel(channel)
                builder.setChannelId(channelId)
            }

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        }
    }
}