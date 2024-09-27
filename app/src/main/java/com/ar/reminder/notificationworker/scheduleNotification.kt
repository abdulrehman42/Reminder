package com.ar.reminder.notificationworker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.ar.reminder.model.ListResponseModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun scheduleNotifications(context: Context, listData: List<ListResponseModel.ResponseModelItem?>) {
    if (listData.isNullOrEmpty()) return  // Return if listData is null or empty

    // Define the time format
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    // Get the current time
    val currentTime = Calendar.getInstance()

    // Iterate over each item in listData
    for (item in listData) {
        item?.scheduleV2?.let { scheduleV2 ->
            val dailyRepeatValues = scheduleV2.dailyRepeatValues

            // Iterate through each day and schedule notifications
            for (day in listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")) {
                val timeList = when (day) {
                    "Mon" -> dailyRepeatValues.Mon
                    "Tue" -> dailyRepeatValues.Tue
                    "Wed" -> dailyRepeatValues.Wed
                    "Thu" -> dailyRepeatValues.Thu
                    "Fri" -> dailyRepeatValues.Fri
                    "Sat" -> dailyRepeatValues.Sat
                    "Sun" -> dailyRepeatValues.Sun
                    else -> emptyList()
                }

                // Schedule notifications for each time in the list
                timeList?.forEach { timeString ->
                    try {
                        val time = timeFormat.parse(timeString)
                        val scheduledTime = getNextOccurrence(day, time)

                        // Calculate the delay for the notification
                        val delay = scheduledTime.timeInMillis - currentTime.timeInMillis
                        if (delay > 0) {
                            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                .setInputData(workDataOf("notification_id" to item._id)) // Send item._id as data
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

// Function to get the next occurrence of the scheduled day
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

    // Set the hour and minute from the time
    time?.let {
        calendar.set(Calendar.HOUR_OF_DAY, it.hours)
        calendar.set(Calendar.MINUTE, it.minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    // Get the target day and calculate the days to add
    val targetDay = dayMap[dayOfWeek]
    val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

    var daysToAdd = targetDay!! - currentDay
    if (daysToAdd < 0 || (daysToAdd == 0 && calendar.timeInMillis <= System.currentTimeMillis())) {
        daysToAdd += 7 // Schedule for the next week
    }

    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)

    return calendar
}