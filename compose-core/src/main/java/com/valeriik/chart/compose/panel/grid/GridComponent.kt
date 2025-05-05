package com.valeriik.chart.compose.panel.grid

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.valeriik.chart.core.renderers.GridOutput

@Composable
fun GridComponent(
    modifier: Modifier,
    gridOutput: GridOutput,
    lineColor: Color,
    verticalMode: GridMode,
    horizontalMode: GridMode
) {

    val yPositions = gridOutput.yValues

    val xPositions = gridOutput.xValues

    Canvas(modifier = modifier) {
        yPositions.forEach {
            drawLine(
                gridMode = horizontalMode,
                lineColor = lineColor,
                start = Offset(0f, it),
                end = Offset(size.width, it),
            )
        }
        xPositions.forEach {
            drawLine(
                gridMode = verticalMode,
                lineColor = lineColor,
                start = Offset(it, 0f),
                end = Offset(it, size.height)
            )

        }
    }
}

fun DrawScope.drawLine(
    gridMode: GridMode, lineColor: Color, start: Offset, end: Offset
) {

    when (gridMode) {
        is GridMode.Dashed -> {
            drawLine(
                color = lineColor,
                start = start,
                end = end,
                pathEffect = gridMode.pathEffect,
                strokeWidth = gridMode.strokeDp.toPx()
            )
        }

        is GridMode.Stroke -> {
            drawLine(
                color = lineColor, start = start, end = end, strokeWidth = gridMode.strokeDp.toPx()
            )
        }

        else -> {
            //do nothing.
        }
    }
}