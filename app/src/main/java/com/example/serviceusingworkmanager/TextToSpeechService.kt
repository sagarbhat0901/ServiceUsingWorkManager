package com.example.serviceusingworkmanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class TextToSpeechService : Service(), TextToSpeech.OnInitListener {

    private lateinit var textToSpeech: TextToSpeech
    private var reminderText: String? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("mytag","service created")

        textToSpeech = TextToSpeech(this, this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        reminderText = "Reminder music will play for 5 sec. and this is going to happen in two minutes dude, be happy"
        Log.i("mytag","entered service")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("mytag", "Language data is missing or language not supported")
            }
            else{
                reminderText?.let { textToSpeech.speak(it, TextToSpeech.QUEUE_FLUSH, null, null) }
            }
        } else {
            Log.e("mytag", "Initialization failed")
        }
    }
}
