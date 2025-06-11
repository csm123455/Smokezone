package com.smokezone.ui.notification

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.smokezone.databinding.ActivityNotificationBinding
import com.smokezone.worker.AlarmWorker
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

class NotificationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNotificationBinding.inflate(layoutInflater) }

    private val adapter = NotiAdapter { deleteNotificationData(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.notificationList.adapter = adapter

        adapter.setData(getNotificationData())

        binding.add.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                saveNotificationData(getNotificationData() + NotificationData(hour, minute, UUID.randomUUID()))
            }, 0, 0, false).show()
        }
    }

    // prefs 에 저장된 시간을 가져오는 함수
    private fun getNotificationData(): List<NotificationData> {
        val prefs = getSharedPreferences("notification", MODE_PRIVATE)
        val data = prefs.getString("data", null)
        return if (data == null) {
            emptyList()
        } else {
            Gson().fromJson(data, Array<NotificationData>::class.java).toList()
        }
    }

    // prefs 에 List<NotificationData> 를 gson형식으로 저장하는 함수
    private fun saveNotificationData(data: List<NotificationData>) {
        val prefs = getSharedPreferences("notification", MODE_PRIVATE)
        prefs.edit().putString("data", Gson().toJson(data)).apply()

        adapter.setData(getNotificationData())
        adapter.notifyDataSetChanged()

        if (data.isEmpty()) return

        val dailyWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(getTimeUsingInWorkRequest(data.last()), TimeUnit.MILLISECONDS)
            .setId(data.last().uuid)
            .build()

        WorkManager.getInstance(this).enqueue(dailyWorkRequest)
    }

    // prefs에 저장된 시간을 삭제하는 것
    private fun deleteNotificationData(notificationData: NotificationData) {
        val data = getNotificationData().toMutableList()
        data.remove(notificationData)
        saveNotificationData(data)

        adapter.setData(data)
        adapter.notifyDataSetChanged()

        val workManager = WorkManager.getInstance(this)
        workManager.cancelWorkById(notificationData.uuid)
    }


    private fun getTimeUsingInWorkRequest(notificationData: NotificationData) : Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, notificationData.hour)
        dueDate.set(Calendar.MINUTE, notificationData.minute)
        dueDate.set(Calendar.SECOND, 0)

        if(dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}
