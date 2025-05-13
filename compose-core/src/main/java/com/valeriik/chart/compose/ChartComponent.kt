package com.valeriik.chart.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.valeriik.chart.compose.panel.XAxisScope
import com.valeriik.chart.compose.panel.date.DatePanelComponent
import com.valeriik.chart.compose.panel.grid.GridComponent
import com.valeriik.chart.compose.panel.value.ValuePanelComponent
import com.valeriik.chart.compose.panel.value.ValuePanelScope
import com.valeriik.chart.core.axis.DateAxis
import com.valeriik.chart.core.axis.DefaultYAxis
import com.valeriik.chart.core.axis.IndexAxis
import com.valeriik.chart.core.projection.Projection
import com.valeriik.chart.core.renderers.DatePanelRenderer
import com.valeriik.chart.core.renderers.GridPanelRenderer
import com.valeriik.chart.core.renderers.LinearChartRenderer
import com.valeriik.chart.core.renderers.ValueOutput
import com.valeriik.chart.core.renderers.ValuePanelRenderer
import kotlin.math.max
import kotlin.math.min

@Composable
fun ChartComponent(
    modifier: Modifier = Modifier,
    content: ChartScope.() -> Unit,
) {
    val ticksCount = 5

    val chartScope = ChartScope().apply(content)

    val xAxis = remember {
        val xAxisScope = chartScope.xAxis
        when (xAxisScope) {
            is XAxisScope.IndexScope -> IndexAxis(
                itemsPerScreen = xAxisScope.itemsPerScreen,
                dataCount = chartScope.dataPoints.values.firstOrNull()?.size ?: 0
            )

            is XAxisScope.DateScope -> DateAxis(
                initialFromTimestamp = xAxisScope.fromTimestamp,
                initialToTimestamp = xAxisScope.toTimestamp,
            )
        }
    }

    val yProjection = remember { Projection(0f, 1f) }
    val yAxis = remember { DefaultYAxis(yProjection = yProjection) }

    var leftPanelWidth by remember { mutableStateOf(0f) }
    var rightPanelWidth by remember { mutableStateOf(0f) }

    val leftValuePanels = remember(chartScope.valuePanels) {
        chartScope.valuePanels
            .filter { it.value.position == ValuePanelScope.Position.Left }
            .takeIf { it.isNotEmpty() }
    }

    val rightValuePanels = remember(chartScope.valuePanels) {
        chartScope.valuePanels
            .filter { it.value.position == ValuePanelScope.Position.Right }
            .takeIf { it.isNotEmpty() }
    }

    val renderer = remember { LinearChartRenderer(xScale = xAxis, yAxis = yAxis) }
    val valueRenderer = remember { ValuePanelRenderer(yAxis = yAxis) }
    val dateRenderer = remember { DatePanelRenderer(xScale = xAxis) }
    val gridRenderer = remember { GridPanelRenderer(xAxis = xAxis, yAxis = yAxis) }

    var scrollOffset: Float by remember { mutableStateOf(0f) }

    val transformableState = rememberTransformableState { _, pan, zoom ->
        xAxis.scrollBy(pan.x)
        xAxis.zoom(zoom)
        scrollOffset = xAxis.getScrollOffset()
    }

    val visibleIndices by remember(scrollOffset, chartScope.dataPoints) {
        derivedStateOf {
            chartScope.dataPoints.mapValues { (_, dataPoints) ->
                xAxis.getVisibleIndices(dataPoints)
            }
        }
    }

    LaunchedEffect(visibleIndices) {
        var minValue: Float? = null
        var maxValue: Float? = null

        for ((key, indices) in visibleIndices) {
            chartScope.dataPoints[key]?.let { dataPoints ->
                for (index in indices) {
                    val dataPoint = dataPoints.getOrNull(index) ?: continue
                    minValue = minValue?.let { min(it, dataPoint.value) } ?: dataPoint.value
                    maxValue = maxValue?.let { max(it, dataPoint.value) } ?: dataPoint.value
                }
            }
        }

        if (minValue != null && minValue == maxValue) {
            maxValue = minValue!! + 1f
        }

        yProjection.minValue = minValue ?: 0f
        yProjection.maxValue = maxValue ?: 1f

        val gap =
            (yProjection.maxValue - yProjection.minValue) * chartScope.yAxis.verticalSpacingThreshold
        yProjection.minValue -= gap
        yProjection.maxValue += gap
    }

    val valueOutputs = remember(
        visibleIndices,
        yProjection.screenMax,
        yProjection.minValue,
        yProjection.maxValue
    ) {
        chartScope.valuePanels.mapValues { (key, panelScope) ->
            chartScope.dataPoints[key]?.let {
                valueRenderer.render(panelScope.valueFormat, ticks = ticksCount)
            } ?: ValueOutput(emptyList())
        }
    }

    val gridOutput = remember(valueOutputs) {
        gridRenderer.render(
            data = chartScope.dataPoints.values.firstOrNull() ?: emptyList(),
            valueOutput = valueOutputs.values.firstOrNull() ?: ValueOutput(emptyList())
        )
    }

    val dateOutput = remember(visibleIndices, xAxis.getScrollOffset(), yAxis.minValue) {
        chartScope.datePanels.mapValues { (key, dataPanelScope) ->
            val dataPoints = chartScope.dataPoints[key] ?: emptyList()
            dateRenderer.render(dataPoints, dataPanelScope.dateFormat)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = chartScope.backgroundColor)
    ) {
        Row(modifier = Modifier.weight(1f)) {
            leftValuePanels?.let {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .onGloballyPositioned { leftPanelWidth = it.size.width.toFloat() }
                ) {
                    for ((key, panelScope) in it) {
                        val output = valueOutputs[key] ?: continue
                        ValuePanelComponent(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    horizontal = panelScope.horizontalSpacingDp
                                ),
                            output = output,
                            style = panelScope.style,
                            background = panelScope.backgroundColor,
                            arrangement = panelScope.verticalArrangement
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clipToBounds()
                    .onGloballyPositioned {
                        yProjection.screenMax = it.size.height.toFloat()
                        xAxis.updateViewWidth(it.size.width.toFloat())
                    }
                    .transformable(transformableState)
            ) {
                chartScope.gridScope?.let { gridScope ->
                    GridComponent(
                        modifier = Modifier.fillMaxSize(),
                        gridOutput = gridOutput,
                        lineColor = gridScope.color,
                        verticalMode = gridScope.verticalMode,
                        horizontalMode = gridScope.horizontalMode
                    )
                }
                for (entry in chartScope.plots) {
                    val dataPoints = chartScope.dataPoints[entry.key] ?: continue
                    PlotComponent(
                        renderer = renderer,
                        plot = entry.value,
                        dataPoints = dataPoints,
                        scrollOffset = scrollOffset,
                    )
                }
            }
            rightValuePanels?.let {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .onGloballyPositioned { rightPanelWidth = it.size.width.toFloat() }
                ) {
                    for ((key, panelScope) in it) {
                        val output = valueOutputs[key] ?: continue
                        ValuePanelComponent(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    horizontal = panelScope.horizontalSpacingDp
                                ),
                            output = output,
                            style = panelScope.style,
                            background = panelScope.backgroundColor,
                            arrangement = panelScope.verticalArrangement
                        )
                    }
                }
            }
        }
        for ((key, panelScope) in chartScope.datePanels) {
            val output = dateOutput[key] ?: continue
            DatePanelComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = with(LocalDensity.current) {
                        leftPanelWidth.toDp()
                    }, end = with(LocalDensity.current) {
                        rightPanelWidth.toDp()
                    })
                    .clipToBounds()
                    .background(panelScope.backgroundColor)
                    .padding(vertical = panelScope.verticalSpacingDp),
                dateOutput = output,
                style = panelScope.textStyle,
                arrangement = panelScope.arrangement
            )
        }
    }
}