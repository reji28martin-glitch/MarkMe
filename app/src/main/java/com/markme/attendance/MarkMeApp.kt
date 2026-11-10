package com.markme.attendance

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.markme.attendance.data.local.AppDatabase

class MarkMeApp : Application() {
    companion object {
        const val KIOSK_CHANNEL_ID = "kiosk_foreground_channel"
        lateinit var instance: MarkMeApp
            private set
    }

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                KIOSK_CHANNEL_ID,
                "Kiosk Continuous Face Recognition",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Runs background camera and RTSP CCTV stream face recognition"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
