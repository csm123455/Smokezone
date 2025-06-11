package com.smokezone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.smokezone.ui.notification.AlarmReceiver
import com.smokezone.MapActivity
import com.smokezone.ui.notification.NotificationActivity
import com.smokezone.R
import com.smokezone.databinding.ActivityMainBinding
import com.smokezone.startNoSmokeLink
import com.smokezone.ui.calendar.CalendarActivity
import com.smokezone.ui.chat.MessageActivity
import com.smokezone.ui.community.CommunityActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @SuppressLint("MissingPermission")
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

                val name = "내 채널 이름"
                val descriptionText = "내 채널 설명"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("내 채널 ID", name, importance).apply {
                    description = descriptionText
                }


                // 채널을 시스템에 등록
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val intent = Intent(this, CalendarActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val builder = NotificationCompat.Builder(this, "내 채널 ID")
                    .setSmallIcon(R.drawable.baseline_send_24)
                    .setContentTitle("흡연 기록하러 가기")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setShowWhen(false)
                    .setOngoing(true)

                with(NotificationManagerCompat.from(this)) {
                    val notification = builder.build()
                    notification.flags = NotificationCompat.FLAG_ONGOING_EVENT
                    notify("asd", 123, notification)
                }

                val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

                val calendars = listOf(
                    Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, 12)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    },

                    Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, 22)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    },
                )

                calendars.forEach {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        it.timeInMillis,
                        alarmIntent
                    )
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.mapBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        binding.communityBtn.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }

        binding.calendarBtn.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        binding.messageBtn.setOnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }

        binding.noSmokeImageView.setOnClickListener {
            startNoSmokeLink()
        }

        binding.notificationImageView.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        // 알람이 울리는 버튼
        binding.view.setOnClickListener {
            val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

            alarmIntent.send()
        }

        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}