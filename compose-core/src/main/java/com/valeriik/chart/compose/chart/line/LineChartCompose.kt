package com.valeriik.chart.compose.chart.line

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import com.valeriik.chart.core.renderers.LinearChartOutput

@Composable
fun LineChartCompose(
    modifier: Modifier = Modifier,
    output: LinearChartOutput,
    color: Color,
    lineWidth: Float,
    circleRadius: Float
) {
    val progress = 1f

    Canvas(modifier = modifier) {
        if (progress >= 1f) {
            lineChart(
                output,
                color,
                lineWidth,
                circleRadius
            )
        } else {
            clipRect(0f, 0f, size.width * progress, size.height) {
                lineChart(
                    output,
                    color,
                    lineWidth,
                    circleRadius
                )
            }
        }
    }
}

private fun DrawScope.lineChart(
    renderedOutput: LinearChartOutput,
    color: Color = Color.Blue,
    lineWidth: Float,
    circleRadius: Float
) {
    val path = Path()
    for (dataPoint in renderedOutput.dataPoints) {
        if (path.isEmpty) {
            path.moveTo(dataPoint.x, dataPoint.y)
        } else {
            path.lineTo(dataPoint.x, dataPoint.y)
        }

        if (circleRadius > 0f) {
            drawCircle(color, radius = circleRadius, center = Offset(dataPoint.x, dataPoint.y))
        }
    }
    drawPath(path, color, style = Stroke(width = lineWidth))

}