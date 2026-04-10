package com.battify.data

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.app.usage.UsageStatsManager
import android.os.BatteryManager
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AppUsageInfo(
    val packageName: String,
    val usageTimeMillis: Long,
    val percentage: Float
)

data class BatteryInfo(
    val level: Int,
    val isCharging: Boolean,
    val health: Int,
    val status: Int,
    val appUsage: List<AppUsageInfo> = emptyList()
)

class BatteryRepository(private val context: Context) {
    private val _batteryInfo = MutableStateFlow(getBatteryInfo())
    val batteryInfo: StateFlow<BatteryInfo> = _batteryInfo

    fun updateBatteryInfo() {
        _batteryInfo.value = getBatteryInfo()
    }

    private fun getBatteryInfo(): BatteryInfo {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = (level * 100 / scale.toFloat()).toInt()

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        val health: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1

        val appUsage = getAppUsageStats()

        return BatteryInfo(batteryPct, isCharging, health, status, appUsage)
    }

    private fun getAppUsageStats(): List<AppUsageInfo> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            calendar.timeInMillis,
            System.currentTimeMillis()
        )

        if (stats.isNullOrEmpty()) return emptyList()

        val totalTime = stats.sumOf { it.totalTimeInForeground }
        if (totalTime == 0L) return emptyList()

        return stats
            .filter { it.totalTimeInForeground > 0 }
            .map {
                AppUsageInfo(
                    it.packageName,
                    it.totalTimeInForeground,
                    (it.totalTimeInForeground.toFloat() / totalTime) * 100
                )
            }
            .sortedByDescending { it.usageTimeMillis }
            .take(10)
    }
}
