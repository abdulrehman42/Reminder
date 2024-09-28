package com.ar.reminder.notificationworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ar.reminder.utils.Constants.NOFICATION_MSG
import com.ar.reminder.utils.Constants.NOTIFICATION_ID
import com.ar.reminder.utils.Constants.SOUND_URL_ID
import com.ar.reminder.utils.Helper.playMp3FromUrl
import com.ar.reminder.views.activity.ListingActivity

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val notificationId = inputData.getString(NOTIFICATION_ID) ?: "default_id"
        val noticationSoundUrl=inputData.getString(SOUND_URL_ID)
        val notificationMsg=inputData.getString(NOFICATION_MSG)
        showNotification(notificationId,noticationSoundUrl,notificationMsg)
        return Result.success()
    }

    private fun showNotification(
        notificationId: String,
        noticationSoundUrl: String?,
        notificationMsg: String?
    ) {
        val channelId = "reminder_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminder Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(applicationContext, ListingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Reminder")
            .setContentText(notificationMsg)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(notificationId.hashCode(), notification)
        noticationSoundUrl?.let {
           playMp3FromUrl(it)
       }
    }
}