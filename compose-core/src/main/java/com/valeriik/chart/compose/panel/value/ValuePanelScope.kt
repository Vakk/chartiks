package com.valeriik.chart.compose.panel.value

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ValuePanelScope {
    var valueFormat: String = "%.2f"
    var style: TextStyle = TextStyle.Default
    var backgroundColor: Color = Color.Transparent
    var position = Position.Left
    var horizontalSpacingDp: Dp = 0.dp
    var verticalArrangement: Arrangement.Vertical = Arrangement.Center

    enum class Position {
        Left,
        Right
    }
}