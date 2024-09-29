package com.ar.reminder.notificationworker

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.utils.Constants.NOFICATION_MSG
import com.ar.reminder.utils.Constants.NOTIFICATION_ID
import com.ar.reminder.utils.Constants.SOUND_URL_ID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun scheduleNotifications(context: Context, listData: List<ListResponseModel.ResponseModelItem?>) {
    if (listData.isNullOrEmpty()) return
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val currentTime = Calendar.getInstance()
    for (item in listData) {
        item?.scheduleV2?.let { scheduleV2 ->
            val dailyRepeatValues = scheduleV2.dailyRepeatValues

            for (day in listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")) {
                val timeList = when (day) {
                    "Mon" -> dailyRepeatValues?.Mon
                    "Tue" -> dailyRepeatValues?.Tue
                    "Wed" -> dailyRepeatValues?.Wed
                    "Thu" -> dailyRepeatValues?.Thu
                    "Fri" -> dailyRepeatValues?.Fri
                    "Sat" -> dailyRepeatValues?.Sat
                    "Sun" -> dailyRepeatValues?.Sun
                    else -> emptyList()
                }

                timeList?.forEach { timeString ->
                    try {
                        val time = timeFormat.parse(timeString)
                        val scheduledTime = getNextOccurrence(day, time)

                        val delay = scheduledTime.timeInMillis - currentTime.timeInMillis
                        Log.d("NotificationSchedule", "Scheduling notification for $day at ${scheduledTime.time}, delay: $delay ms")

                        if (delay > 0) {
                            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                .setInputData(workDataOf(NOTIFICATION_ID to item._id))
                                .setInputData(workDataOf(NOFICATION_MSG to item.name))
                                .setInputData(workDataOf(SOUND_URL_ID to item.notifsV2SoundUrl))
                                .build()
                            WorkManager.getInstance(context).enqueue(workRequest)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
private fun getNextOccurrence(dayOfWeek: String, time: Date?): Calendar {
    val calendar = Calendar.getInstance()
    val dayMap = mapOf(
        "Mon" to Calendar.MONDAY,
        "Tue" to Calendar.TUESDAY,
        "Wed" to Calendar.WEDNESDAY,
        "Thu" to Calendar.THURSDAY,
        "Fri" to Calendar.FRIDAY,
        "Sat" to Calendar.SATURDAY,
        "Sun" to Calendar.SUNDAY
    )
    time?.let {
        calendar.set(Calendar.HOUR_OF_DAY, it.hours)
        calendar.set(Calendar.MINUTE, it.minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
    val targetDay = dayMap[dayOfWeek]
    val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

    var daysToAdd = targetDay!! - currentDay
    if (daysToAdd < 0 || (daysToAdd == 0 && calendar.timeInMillis <= System.currentTimeMillis())) {
        daysToAdd += 7
    }
    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)
    return calendar
}