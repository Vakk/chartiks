package com.valeriik.chart.compose.panel.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.DateFormat

class DatePanelScope {
    var backgroundColor: Color = Color.Transparent
    var textStyle: TextStyle = TextStyle()
    var dateFormat: DateFormat = DateFormat.getDateInstance()
    var verticalSpacingDp: Dp = 0f.dp
    var arrangement: Arrangement.Horizontal = Arrangement.Start
}