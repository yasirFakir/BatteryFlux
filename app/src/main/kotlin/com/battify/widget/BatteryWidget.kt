package com.battify.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

    override suspend fun provideContent(context: Context, id: GlanceId) {
        val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
        val level = prefs[LEVEL_KEY] ?: 0
        val isCharging = prefs[IS_CHARGING_KEY] ?: false
        val primaryColor = prefs[PRIMARY_COLOR_KEY] ?: 0xFF6650a4

        provideContent {
            BatteryWidgetLayout(level, isCharging, primaryColor)
        }
    }

    companion object {
        val LEVEL_KEY = androidx.datastore.preferences.core.intPreferencesKey("battery_level")
        val IS_CHARGING_KEY = androidx.datastore.preferences.core.booleanPreferencesKey("is_charging")
        val PRIMARY_COLOR_KEY = androidx.datastore.preferences.core.longPreferencesKey("primary_color")

        suspend fun update(context: Context, batteryInfo: BatteryInfo, settings: WidgetSettings) {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(BatteryWidget::class.java)
            glanceIds.forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[LEVEL_KEY] = batteryInfo.level
                    prefs[IS_CHARGING_KEY] = batteryInfo.isCharging
                    prefs[PRIMARY_COLOR_KEY] = settings.primaryColor
                }
                BatteryWidget().update(context, glanceId)
            }
        }
    }
}

@Composable
fun BatteryWidgetLayout(level: Int, isCharging: Boolean, primaryColor: Long) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(androidx.glance.appwidget.R.drawable.glance_component_outline)) // Fallback outline
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Simple 1x1 circular-ish background
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(primaryColor)))
                .padding(8.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = "$level%",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            if (isCharging) {
                Text(
                    text = "⚡",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}
