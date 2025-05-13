package com.valeriik.chart.compose.chart.mountain

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.valeriik.chart.compose.PlotScope

class MountainLinePlotScope: PlotScope {
    var lineColor: Color = Color.Red
    var lineWidth: Float = 0f
    var fillColor: Color = Color.Blue
    var brush: Brush? = null
}