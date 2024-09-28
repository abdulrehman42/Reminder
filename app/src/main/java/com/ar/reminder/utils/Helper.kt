package com.ar.reminder.utils

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import com.ar.reminder.model.ListResponseModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Helper {
    fun getCurrentTime(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun currentDaySchedule(list: ListResponseModel.ResponseModelItem?): String {
        return list?.scheduleV2?.dailyRepeatValues?.let { repeatValues ->
            val currentDay = SimpleDateFormat("EEE", Locale.getDefault()).format(Date())

            val todaySchedule = when (currentDay) {
                "Mon" -> repeatValues.Mon
                "Tue" -> repeatValues.Tue
                "Wed" -> repeatValues.Wed
                "Thu" -> repeatValues.Thu
                "Fri" -> repeatValues.Fri
                "Sat" -> repeatValues.Sat
                "Sun" -> repeatValues.Sun
                else -> emptyList()
            }

            if (todaySchedule.isNullOrEmpty()) {
                "No schedule available for today"
            } else {
                todaySchedule.joinToString(separator = ", ")
            }
        } ?: "No schedule available"
    }


    fun playMp3FromUrl(url: String) {
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(url) // Set the URL of the MP3 file
            mediaPlayer.prepareAsync() // Prepare the media player asynchronously
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start() // Start playback once the media is prepared
            }
            mediaPlayer.setOnCompletionListener {
                it.release() // Release resources when playback is complete
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}