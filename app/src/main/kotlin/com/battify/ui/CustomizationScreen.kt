package com.battify.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.battify.data.WidgetSettings
import kotlinx.coroutines.launch

@Composable
fun CustomizationScreen(
    settings: WidgetSettings,
    onPrimaryColorChange: (Long) -> Unit,
    onShowPercentageToggle: (Boolean) -> Unit,
    onShowChargingStatusToggle: (Boolean) -> Unit,
    onShowAppUsageToggle: (Boolean) -> Unit
) {
    val predefinedColors = listOf(
        0xFF6650a4, 0xFF03DAC5, 0xFFCF6679, 0xFF3700B3, 
        0xFF00C853, 0xFFFFD600, 0xFFFF6D00, 0xFF2962FF
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Widget Customization",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Color Picker
        Text(
            text = "Primary Color",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(predefinedColors) { colorValue ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(colorValue))
                        .clickable { onPrimaryColorChange(colorValue) }
                        .then(
                            if (settings.primaryColor == colorValue) {
                                Modifier.background(Color.White.copy(alpha = 0.3f))
                            } else Modifier
                        )
                )
            }
        }

        Divider(modifier = Modifier.padding(bottom = 24.dp))

        // Toggles
        ToggleItem(
            label = "Show Battery Percentage",
            checked = settings.showPercentage,
            onCheckedChange = onShowPercentageToggle
        )
        ToggleItem(
            label = "Show Charging Status",
            checked = settings.showChargingStatus,
            onCheckedChange = onShowChargingStatusToggle
        )
        ToggleItem(
            label = "Show App Usage",
            checked = settings.showAppUsage,
            onCheckedChange = onShowAppUsageToggle
        )
    }
}

@Composable
fun ToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
