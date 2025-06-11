package com.smokezone.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.smokezone.R
import com.smokezone.ui.calendar.CalendarActivity
import kotlin.random.Random


class AlarmReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "CHANNEL_ID"

    override fun onReceive(context: Context?, p1: Intent?) {
        context ?: return
        createNotificationChannel(context)

        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java) as NotificationManager

        val intent = Intent(context, CalendarActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_send_24)
            .setContentTitle("금연")
            .setContentText("도전하세요!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setShowWhen(false)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }


    private fun createNotificationChannel(context: Context?) {
            val name = "Notification_Ch"
            val descriptionText = "Test Notification"
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
    }

}