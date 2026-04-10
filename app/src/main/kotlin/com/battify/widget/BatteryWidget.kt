package com.battify.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.battify.data.BatteryInfo
import com.battify.data.WidgetSettings

class BatteryWidget : GlanceAppWidget() {
    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        val LEVEL_KEY = androidx.datastore.preferences.core.intPreferencesKey("battery_level")
        val IS_CHARGING_KEY = androidx.datastore.preferences.core.booleanPreferencesKey("is_charging")
        val PRIMARY_COLOR_KEY = androidx.datastore.preferences.core.longPreferencesKey("primary_color")
        val SHOW_PERCENTAGE_KEY = androidx.datastore.preferences.core.booleanPreferencesKey("show_percentage")

        private val SMALL_SQUARE = DpSize(40.dp, 40.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(100.dp, 40.dp)
        private val BIG_SQUARE = DpSize(100.dp, 100.dp)

        suspend fun update(context: Context, batteryInfo: BatteryInfo, settings: WidgetSettings) {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(BatteryWidget::class.java)
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[LEVEL_KEY] = batteryInfo.level
                    prefs[IS_CHARGING_KEY] = batteryInfo.isCharging
                    prefs[PRIMARY_COLOR_KEY] = settings.primaryColor
                    prefs[SHOW_PERCENTAGE_KEY] = settings.showPercentage
                }
                BatteryWidget().update(context, glanceId)
            }
        }
    }

    override suspend fun provideContent(context: Context, id: GlanceId) {
        val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
        val level = prefs[LEVEL_KEY] ?: 0
        val isCharging = prefs[IS_CHARGING_KEY] ?: false
        val primaryColor = prefs[PRIMARY_COLOR_KEY] ?: 0xFF6650a4
        val showPercentage = prefs[SHOW_PERCENTAGE_KEY] ?: true

        provideContent {
            val size = LocalSize.current
            BatteryWidgetResponsiveLayout(level, isCharging, primaryColor, showPercentage, size)
        }
    }
}

@Composable
fun BatteryWidgetResponsiveLayout(
    level: Int, 
    isCharging: Boolean, 
    primaryColor: Long, 
    showPercentage: Boolean,
    size: DpSize
) {
    val backgroundColor = Color(primaryColor)
    
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(backgroundColor))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (size.width < 100.dp) {
            // 1x1 Small Layout
            Column(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
                if (showPercentage) {
                    Text(
                        text = "$level%",
                        style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
                if (isCharging) {
                    Text(text = "⚡", style = TextStyle(color = ColorProvider(Color.White), fontSize = 10.sp))
                }
            }
        } else {
            // Larger Layout
            Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                if (isCharging) {
                    Text(text = "⚡ ", style = TextStyle(color = ColorProvider(Color.White), fontSize = 20.sp))
                }
                Column(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
                    if (showPercentage) {
                        Text(
                            text = "$level%",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        )
                    }
                    Text(
                        text = "Battery",
                        style = TextStyle(color = ColorProvider(Color.White).apply {  }, fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
