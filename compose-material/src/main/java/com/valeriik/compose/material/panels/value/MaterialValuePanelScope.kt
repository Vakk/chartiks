package com.valeriik.compose.material.panels.value

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

class MaterialValuePanelScope {
    var valueFormat = "%.2f"
    var ticks = 3
    var textStyle = TextStyle.Default.copy(Color.DarkGray)
    var tickLineSize = 24f
    var tickWidth = 1f
    var tickColor: Color = Color.LightGray
    var innerSpacing = 16f
}