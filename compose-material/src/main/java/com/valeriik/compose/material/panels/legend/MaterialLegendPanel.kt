package com.valeriik.compose.material.panels.legend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.valeriik.chart.compose.model.Legend

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MaterialLegendPanel(
    modifier: Modifier = Modifier,
    scope: MaterialLegendPanelScope,
    data: List<Legend>
) {
    val spacing = with(LocalDensity.current) {
        scope.spacing.toDp()
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.Center
    ) {
        data.forEach { MaterialLegendItem(it.color, it.label, scope) }
    }
}

@Composable
internal fun MaterialLegendItem(color: Color, label: String, scope: MaterialLegendPanelScope) {
    val spacing = with(LocalDensity.current) {
        scope.spacing.toDp()
    }
    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
        Canvas(modifier = Modifier.size(16.dp), onDraw = {
            if (scope.type is MaterialLegendPanelScope.Type.Circle) {
                drawCircle(color = color)
            } else {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height.div(2f)),
                    end = Offset(size.width, size.height.div(2f)),
                    strokeWidth = size.width / 3f
                )
            }
        })
        Text(text = label, style = scope.textStyle)
    }

}