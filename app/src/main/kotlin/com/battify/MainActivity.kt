package com.battify

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.battify.data.BatteryRepository
import com.battify.data.WidgetSettings
import com.battify.data.WidgetSettingsRepository
import com.battify.service.BatteryService
import com.battify.ui.CustomizationScreen
import com.battify.ui.DashboardScreen
import com.battify.ui.theme.BatteryFluxTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var batteryRepository: BatteryRepository
    private lateinit var widgetSettingsRepository: WidgetSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        batteryRepository = BatteryRepository(this)
        widgetSettingsRepository = WidgetSettingsRepository(this)
        
        requestPermissions()
        if (!hasUsageStatsPermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        startBatteryService()

        setContent {
            BatteryFluxTheme {
                val batteryInfo by batteryRepository.batteryInfo.collectAsState()
                val widgetSettings by widgetSettingsRepository.settingsFlow.collectAsState(initial = WidgetSettings())
                var selectedTab by remember { mutableIntStateOf(0) }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") },
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") },
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (selectedTab) {
                            0 -> DashboardScreen(batteryInfo = batteryInfo)
                            1 -> CustomizationScreen(
                                settings = widgetSettings,
                                onPrimaryColorChange = { color ->
                                    lifecycleScope.launch { widgetSettingsRepository.updatePrimaryColor(color) }
                                },
                                onShowPercentageToggle = { show ->
                                    lifecycleScope.launch { widgetSettingsRepository.updateShowPercentage(show) }
                                },
                                onShowChargingStatusToggle = { show ->
                                    lifecycleScope.launch { widgetSettingsRepository.updateShowChargingStatus(show) }
                                },
                                onShowAppUsageToggle = { show ->
                                    lifecycleScope.launch { widgetSettingsRepository.updateShowAppUsage(show) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        batteryRepository.updateBatteryInfo()
    }

    private fun startBatteryService() {
        val intent = Intent(this, BatteryService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { _ -> }
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
