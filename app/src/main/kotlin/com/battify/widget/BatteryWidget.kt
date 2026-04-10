package com.battify.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.battify.R

class BatteryWidget : GlanceAppWidget() {
    override suspend fun provideContent(context: Context, id: GlanceId) {
        provideContent {
            BatteryWidgetLayout(context)
        }
    }
}

@Composable
fun BatteryWidgetLayout(context: Context) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(androidx.compose.ui.graphics.Color.DarkGray))
            .padding(8.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(
            text = "100%",
            style = TextStyle(
                color = ColorProvider(androidx.compose.ui.graphics.Color.White),
                fontSize = 16.sp
            )
        )
    }
}
