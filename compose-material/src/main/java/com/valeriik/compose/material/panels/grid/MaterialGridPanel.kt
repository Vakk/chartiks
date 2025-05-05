package com.valeriik.compose.material.panels.grid

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.valeriik.chart.core.renderers.GridOutput

@Composable
fun MaterialGridPanel(
    modifier: Modifier = Modifier,
    gridOutput: GridOutput,
    gridScope: MaterialGridScope
) {

    Canvas(modifier = modifier.fillMaxSize()) {
        gridOutput.xValues.forEach {
            drawLine(
                gridScope.lineColor,
                start = Offset(it, 0f),
                end = Offset(it, size.height),
                strokeWidth = gridScope.strokeWidth
            )
        }
        gridOutput.yValues.forEach {
            drawLine(
                gridScope.lineColor,
                start = Offset(0f, it),
                end = Offset(size.width, it),
                strokeWidth = gridScope.strokeWidth
            )
        }
    }
}