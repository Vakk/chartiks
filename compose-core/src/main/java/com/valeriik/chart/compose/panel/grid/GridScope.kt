package com.valeriik.chart.compose.panel.grid

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

class GridScope {
    var color = Color.Gray
    var verticalMode: GridMode = GridMode.Dashed(.5f.dp, PathEffect.dashPathEffect(floatArrayOf(16f, 12f)))
    var horizontalMode: GridMode = verticalMode
}