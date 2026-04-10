package com.battify.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.battify.data.AppUsageInfo
import com.battify.data.BatteryInfo

@Composable
fun DashboardScreen(batteryInfo: BatteryInfo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Battery Status",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        BatteryCircle(level = batteryInfo.level, isCharging = batteryInfo.isCharging)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "App Usage (Last 24h)",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (batteryInfo.appUsage.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No usage data available. Ensure permission is granted.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(batteryInfo.appUsage) { app ->
                    AppUsageItem(app)
                }
            }
        }
    }
}

@Composable
fun BatteryCircle(level: Int, isCharging: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        CircularProgressIndicator(
            progress = level / 100f,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 12.dp,
            color = if (level > 20) Color.Green else Color.Red,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$level%",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (isCharging) {
                Text(text = "Charging", color = Color.Gray)
            }
        }
    }
}

@Composable
fun AppUsageItem(app: AppUsageInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = app.packageName, fontWeight = FontWeight.Medium, maxLines = 1)
                Text(
                    text = "${app.usageTimeMillis / 60000} mins",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${String.format("%.1f", app.percentage)}%",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
