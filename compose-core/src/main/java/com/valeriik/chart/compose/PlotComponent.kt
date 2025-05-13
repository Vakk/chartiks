package com.valeriik.chart.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import com.valeriik.chart.compose.chart.line.LineChartCompose
import com.valeriik.chart.compose.chart.line.LinePlotScope
import com.valeriik.chart.compose.chart.mountain.MountainChartCompose
import com.valeriik.chart.compose.chart.mountain.MountainLinePlotScope
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.chart.core.renderers.LinearChartOutput
import com.valeriik.chart.core.renderers.LinearChartRenderer

@Composable
internal fun PlotComponent(
    renderer: LinearChartRenderer<CartesianDataPoint>,
    plot: PlotScope,
    scrollOffset: Float,
    dataPoints: List<CartesianDataPoint>,
) {
    var plotWidth by remember { mutableFloatStateOf(0f) }
    var plotHeight by remember { mutableFloatStateOf(0f) }
    val output = remember(scrollOffset, renderer, dataPoints, plotWidth, plotHeight) {
        if (plotWidth == 0f || plotHeight == 0f) {
            LinearChartOutput(emptyList())
        } else {
            renderer.render(dataPoints)
        }
    }

    when (plot) {
        is LinePlotScope -> {
            LineChartCompose(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned {
                        plotWidth = it.size.width.toFloat()
                        plotHeight = it.size.width.toFloat()
                    },
                output = output,
                color = plot.color,
                lineWidth = plot.width,
                circleRadius = plot.circleRadius
            )
        }

        is MountainLinePlotScope -> {
            MountainChartCompose(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned {
                        plotWidth = it.size.width.toFloat()
                        plotHeight = it.size.width.toFloat()
                    },
                output = output,
                lineColor = plot.lineColor,
                fillColor = plot.fillColor,
                lineWidth = plot.lineWidth,
                brush = plot.brush,
            )
        }
    }
}