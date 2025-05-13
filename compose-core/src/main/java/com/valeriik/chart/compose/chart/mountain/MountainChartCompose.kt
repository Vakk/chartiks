package com.valeriik.chart.compose.chart.mountain

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import com.valeriik.chart.core.renderers.LinearChartOutput


@Composable
fun MountainChartCompose(
    modifier: Modifier = Modifier,
    output: LinearChartOutput,
    lineColor: Color,
    fillColor: Color,
    lineWidth: Float,
    brush: Brush? = null
) {
    val progress = 1f

    Canvas(modifier = modifier) {
        if (progress >= 1f) {
            mountainChart(
                output,
                lineColor = lineColor,
                fillColor = fillColor,
                lineWidth = lineWidth,
                brush = brush
            )
        } else {
            clipRect(0f, 0f, size.width * progress, size.height) {
                mountainChart(
                    output,
                    lineColor = lineColor,
                    fillColor = fillColor,
                    lineWidth = lineWidth,
                    brush = brush
                )
            }
        }
    }
}

private fun DrawScope.mountainChart(
    renderedOutput: LinearChartOutput,
    lineColor: Color = Color.Blue,
    fillColor: Color = Color.Blue,
    lineWidth: Float,
    brush: Brush?
) {
    val path = Path()
    renderedOutput.dataPoints.forEach {
        if (path.isEmpty) {
            path.moveTo(it.x, size.height)
        }
        path.lineTo(it.x, it.y)
    }
    renderedOutput.dataPoints.lastOrNull()?.let {
        path.lineTo(it.x, size.height)
    }
    path.close()
    if (lineWidth > 0f) {
        drawPath(path, lineColor, style = Stroke(lineWidth))
    }
    brush?.let {
        drawPath(path, it, style = Fill)
    } ?: let {
        drawPath(path, fillColor, style = Fill)
    }
}