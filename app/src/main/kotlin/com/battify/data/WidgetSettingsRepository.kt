package com.battify.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "widget_settings")

data class WidgetSettings(
    val primaryColor: Long = 0xFF6650a4, // Default primary
    val textColor: Long = 0xFFFFFFFF,
    val showPercentage: Boolean = true,
    val showChargingStatus: Boolean = true,
    val showAppUsage: Boolean = true
)

class WidgetSettingsRepository(private val context: Context) {
    companion object {
        val PRIMARY_COLOR = longPreferencesKey("primary_color")
        val TEXT_COLOR = longPreferencesKey("text_color")
        val SHOW_PERCENTAGE = booleanPreferencesKey("show_percentage")
        val SHOW_CHARGING_STATUS = booleanPreferencesKey("show_charging_status")
        val SHOW_APP_USAGE = booleanPreferencesKey("show_app_usage")
    }

    val settingsFlow: Flow<WidgetSettings> = context.dataStore.data.map { preferences ->
        WidgetSettings(
            primaryColor = preferences[PRIMARY_COLOR] ?: 0xFF6650a4,
            textColor = preferences[TEXT_COLOR] ?: 0xFFFFFFFF,
            showPercentage = preferences[SHOW_PERCENTAGE] ?: true,
            showChargingStatus = preferences[SHOW_CHARGING_STATUS] ?: true,
            showAppUsage = preferences[SHOW_APP_USAGE] ?: true
        )
    }

    suspend fun updatePrimaryColor(color: Long) {
        context.dataStore.edit { it[PRIMARY_COLOR] = color }
    }

    suspend fun updateTextColor(color: Long) {
        context.dataStore.edit { it[TEXT_COLOR] = color }
    }

    suspend fun updateShowPercentage(show: Boolean) {
        context.dataStore.edit { it[SHOW_PERCENTAGE] = show }
    }

    suspend fun updateShowChargingStatus(show: Boolean) {
        context.dataStore.edit { it[SHOW_CHARGING_STATUS] = show }
    }

    suspend fun updateShowAppUsage(show: Boolean) {
        context.dataStore.edit { it[SHOW_APP_USAGE] = show }
    }
}
