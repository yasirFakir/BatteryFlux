package com.battify.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.battify.MainActivity
import com.battify.data.BatteryRepository
import com.battify.data.WidgetSettings
import com.battify.data.WidgetSettingsRepository
import com.battify.widget.BatteryWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BatteryService : Service() {

    private lateinit var batteryRepository: BatteryRepository
    private lateinit var widgetSettingsRepository: WidgetSettingsRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val CHANNEL_ID = "BatteryServiceChannel"
    private val NOTIFICATION_ID = 1

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                batteryRepository.updateBatteryInfo()
                updateNotification()
                updateWidget()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        batteryRepository = BatteryRepository(this)
        widgetSettingsRepository = WidgetSettingsRepository(this)
        createNotificationChannel()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    private fun createNotification(): Notification {
        val batteryInfo = batteryRepository.batteryInfo.value
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Battery Status")
            .setContentText("${batteryInfo.level}% - ${if (batteryInfo.isCharging) "Charging" else "Discharging"}")
            .setSmallIcon(android.R.drawable.ic_lock_idle_low_battery)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun updateWidget() {
        serviceScope.launch {
            val settings = widgetSettingsRepository.settingsFlow.first()
            BatteryWidget.update(this@BatteryService, batteryRepository.batteryInfo.value, settings)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Battery Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
