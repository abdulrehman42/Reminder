package com.ar.reminder.utils

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Helper {
    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return currentTime.format(formatter)
    }

    private var mediaPlayer: MediaPlayer? = null

    fun playMp3FromUrl(context: Context, url: String, start: Boolean) {
        if (start) {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                    it.reset()
                }
            }

            // Initialize the MediaPlayer
            mediaPlayer = MediaPlayer()

            try {
                mediaPlayer?.setDataSource(url)
                mediaPlayer?.setOnPreparedListener {
                    it.start() // Start playing the MP3 when prepared
                    Toast.makeText(context, "Playing MP3", Toast.LENGTH_SHORT).show()
                }
                mediaPlayer?.setOnErrorListener { _, what, extra ->
                    Toast.makeText(context, "Error: $what, Extra: $extra", Toast.LENGTH_SHORT).show()
                    false
                }
                mediaPlayer?.prepareAsync() // Prepare asynchronously

                // Release the media player when the playback is complete
                mediaPlayer?.setOnCompletionListener {
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                    it.release() // Release the MediaPlayer resources
                    mediaPlayer = null
                    Toast.makeText(context, "Playback stopped", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}