package com.example.serviceusingworkmanager


import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log

import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.sql.Time

import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var getTimeButton: Button
    private lateinit var timeTextView: TextView
    private lateinit var mediaPlayer : MediaPlayer
    private val handler = Handler()
    private lateinit var selectedTime: Calendar
    private lateinit var currentTime: Calendar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.song)
        timePicker = findViewById(R.id.timePicker)
        getTimeButton = findViewById(R.id.button)
        timeTextView = findViewById(R.id.textView)


        getTimeButton.setOnClickListener {

            val hour = timePicker.hour
            val minute = timePicker.minute

            val time = formatTime(hour, minute)
            timeTextView.text = "Selected Time: $time"
            getTimeButton.isEnabled=false

            currentTime = Calendar.getInstance()
            selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)}

            val delay2 = selectedTime.timeInMillis - currentTime.timeInMillis - 10 * 60 * 1000
            handler.postDelayed({
                Log.i("mytag","enter handler")
                startService(Intent(this, TextToSpeechService::class.java))
            }, delay2)


            val delay = selectedTime.timeInMillis - currentTime.timeInMillis
            handler.postDelayed({
                playSongForFiveSeconds()
            }, delay)
        }

    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(calendar.time)
    }


    private fun playSongForFiveSeconds() {
        mediaPlayer.start()
        // Stop the song after 5 seconds
        handler.postDelayed({
            mediaPlayer.stop()
            mediaPlayer.prepare() // Reset MediaPlayer to its uninitialized state
            getTimeButton.isEnabled=true
        }, 5000) // 5000 milliseconds = 5 seconds

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Release resources when activity is destroyed
    }
}