package com.valeriik.compose.material.line

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.valeriik.chart.core.axis.StaticXAxis
import com.valeriik.chart.core.axis.StaticYAxis
import com.valeriik.chart.core.axis.XAxis
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.chart.core.data.Offset
import com.valeriik.chart.core.renderers.DatePanelRenderer
import com.valeriik.chart.core.renderers.GridPanelRenderer
import com.valeriik.chart.core.renderers.LinearChartOutput
import com.valeriik.chart.core.renderers.LinearChartRenderer
import com.valeriik.chart.core.renderers.ValuePanelRenderer
import com.valeriik.chart.core.utils.ChartUtils
import com.valeriik.compose.material.components.MaterialXAxisComponent
import com.valeriik.compose.material.panels.grid.MaterialGridPanel
import com.valeriik.compose.material.panels.value.MaterialValuePanel

@Composable
fun MaterialLineChart(
    modifier: Modifier = Modifier, content: MaterialLineChartScope.() -> Unit
) {

    val chartScope = remember { MaterialLineChartScope().apply(content) }

    val xAxis: XAxis<CartesianDataPoint> = remember { StaticXAxis() }
    val yAxis = remember {
        StaticYAxis(
            minValue = chartScope.data.minOf { it.value },
            maxValue = chartScope.data.maxOf { it.value },
        )
    }

    var chartWidth by remember { mutableFloatStateOf(0f) }
    var chartHeight by remember { mutableFloatStateOf(0f) }

    var valuePanelWidth by remember { mutableFloatStateOf(0f) }

    var selectedIndex by remember {
        mutableIntStateOf(-1)
    }

    val renderer = remember {
        LinearChartRenderer(xScale = xAxis, yAxis = yAxis)
    }
    val valueRenderer = remember {
        ValuePanelRenderer(yAxis = yAxis)
    }
    val dateRenderer = remember {
        DatePanelRenderer(xAxis)
    }
    val gridRenderer = remember {
        GridPanelRenderer(xAxis, yAxis)
    }
    val output =
        remember(chartScope.data, chartHeight, chartWidth) { renderer.render(chartScope.data) }

    val valueOutput = remember(chartScope.data, chartHeight) {
        valueRenderer.render(
            format = chartScope.valuePanelScope.valueFormat,
            ticks = chartScope.valuePanelScope.ticks
        )
    }

    val dateOutput = remember(chartScope.data, chartWidth) {
        dateRenderer.render(chartScope.data, chartScope.datePanelScope.dateFormatter)
    }
    val gridOutput = remember(chartScope.data, chartWidth) {
        gridRenderer.render(valueOutput, dateOutput)
    }
    // Track the offset for X and Y positions
    var selectionTargetX by remember { mutableStateOf(0f) }
    var selectionTargetY by remember { mutableStateOf(0f) }

    // Animate the X and Y offsets with `animateFloatAsState`
    val animatedOffsetX by animateFloatAsState(
        targetValue = selectionTargetX,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    val animatedOffsetY by animateFloatAsState(
        targetValue = selectionTargetY,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )
    Column(modifier) {
        Row(modifier = Modifier.weight(1f)) {
            MaterialValuePanel(
                modifier = Modifier
                    .fillMaxHeight()
                    .onGloballyPositioned {
                        valuePanelWidth = it.size.width.toFloat()
                    },
                valueOutput = valueOutput,
                scope = chartScope.valuePanelScope,
                alignTickToRight = false
            )
            Box(modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    yAxis.updateViewHeight(it.size.height.toFloat())
                    xAxis.updateViewWidth(it.size.width.toFloat())
                    chartWidth = it.size.width.toFloat()
                    chartHeight = it.size.height.toFloat()
                }
                .fillMaxSize()) {
                MaterialGridPanel(
                    modifier = Modifier.fillMaxSize(),
                    gridOutput = gridOutput,
                    gridScope = chartScope.gridPanelScope
                )

                LineChartComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { point ->
                                var tapIndex = -1
                                val tapPoint = Offset(
                                    x = point.x, y = point.y
                                )
                                for (index in output.dataPoints.indices) {
                                    val dataPoint = output.dataPoints[index]
                                    if (ChartUtils.isTapNearPoint(
                                            tapPoint = tapPoint.copy(y = dataPoint.y), dataPoint = dataPoint, radius = 20f
                                        )
                                    ) {
                                        selectionTargetX = dataPoint.x
                                        selectionTargetY = dataPoint.y
                                        tapIndex = index
                                        break
                                    }
                                }

                                selectedIndex = tapIndex
                            }
                        },
                    output = output,
                    lineColor = chartScope.lineColor,
                    lineWidth = chartScope.lineWidth,
                    fillColor = chartScope.fillColor,
                    selectedIndex = selectedIndex
                )
                selectedIndex.takeIf { it in chartScope.data.indices }?.let { index ->
                    val offset = output.dataPoints[index]
                    val dataPoint = chartScope.data[index]

                    chartScope.materialPopupScope.content(
                        Modifier.offset {
                            IntOffset(animatedOffsetX.toInt(), animatedOffsetY.toInt())
                        }.onGloballyPositioned {
                            val positionInParent = it.positionInParent()
                            if (positionInParent.x + it.size.width > chartWidth) {
                                selectionTargetX = chartWidth - it.size.width
                            }
                            if (positionInParent.y + it.size.height > chartHeight) {
                                selectionTargetY = chartHeight - it.size.height
                            }
                        }, offset, dataPoint
                    )
                }
            }
        }
        MaterialXAxisComponent(
            output = dateOutput,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) {
                    valuePanelWidth.toDp()
                })
                .fillMaxWidth(),
            scope = chartScope.datePanelScope
        )
    }
}

@Composable
private fun LineChartComponent(
    modifier: Modifier,
    output: LinearChartOutput,
    lineColor: Color,
    lineWidth: Float,
    fillColor: Color,
    selectedIndex: Int
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(output) {
        animate(0f, 1f, animationSpec = tween(300)) { value, velocity ->
            progress = value
        }
    }

    var selectionScaleValue by remember(selectedIndex) {
        mutableFloatStateOf(1f)
    }

    LaunchedEffect(selectedIndex) {
        selectionScaleValue = 1f
        animate(1f, 2f, animationSpec = tween(500)) { value, velocity ->
            selectionScaleValue = value
        }
    }

    if (output.dataPoints.isEmpty()) {
        return
    }
    Canvas(modifier = modifier) {
        if (progress >= 0.99f) {
            drawOutput(
                output, lineColor, lineWidth, fillColor, selectedIndex, selectionScaleValue
            )
        } else {
            clipRect(
                -lineWidth * 2f, -lineWidth * 2f, right = size.width * progress
            ) {
                drawOutput(
                    output, lineColor, lineWidth, fillColor, selectedIndex, selectionScaleValue
                )
            }
        }
    }
}


private fun DrawScope.drawOutput(
    output: LinearChartOutput,
    lineColor: Color,
    lineWidth: Float,
    fillColor: Color,
    selectedIndex: Int,
    selectionScaleValue: Float
) {
    var lastX = 0f
    var lastY = 0f

    val path = Path()
    for (index in output.dataPoints.indices) {
        val dataPoint = output.dataPoints[index]
        if (index == 0) {
            lastY = dataPoint.y
        }
        drawLine(
            color = lineColor,
            start = androidx.compose.ui.geometry.Offset(lastX, lastY),
            end = androidx.compose.ui.geometry.Offset(dataPoint.x, dataPoint.y),
            strokeWidth = lineWidth
        )
        lastX = dataPoint.x
        lastY = dataPoint.y
        if (path.isEmpty) {
            path.moveTo(dataPoint.x, size.height)
            path.lineTo(dataPoint.x, dataPoint.y)
        } else {
            path.lineTo(dataPoint.x, dataPoint.y)
        }
    }

    path.lineTo(size.width, size.height)

    drawPath(path, color = fillColor, style = Fill)

    val circleRadius = lineWidth * 1.2f
    val selectedCircleRadius = circleRadius.times(selectionScaleValue)

    for (index in output.dataPoints.indices) {
        val offset = output.dataPoints[index]

        drawCircle(
            fillColor,
            radius = if (selectedIndex == index) selectedCircleRadius else circleRadius,
            center = androidx.compose.ui.geometry.Offset(offset.x, offset.y),
            style = Fill
        )
        drawCircle(
            lineColor,
            radius = if (selectedIndex == index) selectedCircleRadius else circleRadius,
            center = androidx.compose.ui.geometry.Offset(offset.x, offset.y),
            style = Stroke(lineWidth.times(0.5f))
        )
    }
}