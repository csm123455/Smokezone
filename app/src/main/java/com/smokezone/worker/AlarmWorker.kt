package com.smokezone.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.smokezone.ui.notification.AlarmReceiver
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // 알림 발생 시 다음날 알림도 발생할 수 있게 설정
        val dailyWorkRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(getTimeUsingInWorkRequest(), TimeUnit.MILLISECONDS)
            .setId(id)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(dailyWorkRequest)

        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        alarmIntent.send()

        return Result.success()
    }

    private fun getTimeUsingInWorkRequest() : Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, LocalDateTime.now().hour)
        dueDate.set(Calendar.MINUTE, LocalDateTime.now().minute)
        dueDate.set(Calendar.SECOND, 0)

        if(dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}