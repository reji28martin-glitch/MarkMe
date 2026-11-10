package com.markme.attendance

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.markme.attendance.service.KioskForegroundService
import com.markme.attendance.ui.screens.KioskScreen
import com.markme.attendance.ui.theme.MarkMeTheme
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen awake for attendance kiosk
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Init Text-to-Speech
        tts = TextToSpeech(this, this)

        // Start background kiosk service
        val serviceIntent = Intent(this, KioskForegroundService::class.java)
        startService(serviceIntent)

        setContent {
            MarkMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KioskScreen(
                        currentCamera = "Front Device Camera",
                        onCameraSelect = {}
                    )
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
        }
    }

    fun speakWelcome(studentName: String) {
        tts?.speak("Welcome, $studentName", TextToSpeech.QUEUE_FLUSH, null, "WELCOME_ID")
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
