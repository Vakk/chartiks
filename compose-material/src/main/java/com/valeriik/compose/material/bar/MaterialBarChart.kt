package com.valeriik.compose.material.bar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.round
import com.valeriik.chart.compose.utils.toCompose
import com.valeriik.chart.compose.utils.toDomain
import com.valeriik.chart.core.axis.StaticSectionedXAxis
import com.valeriik.chart.core.axis.StaticYAxis
import com.valeriik.chart.core.data.CategoryDataPoint
import com.valeriik.chart.core.renderers.BarChartOutput
import com.valeriik.chart.core.renderers.BarChartRenderer
import com.valeriik.chart.core.renderers.CategoryPanelRenderer
import com.valeriik.chart.core.renderers.GridPanelRenderer
import com.valeriik.chart.core.renderers.ValuePanelRenderer
import com.valeriik.chart.core.utils.ChartUtils
import com.valeriik.compose.material.components.MaterialXAxisComponent
import com.valeriik.compose.material.components.SimplePopup
import com.valeriik.compose.material.panels.grid.MaterialGridPanel
import com.valeriik.compose.material.panels.value.MaterialValuePanel

@Composable
fun MaterialBarChart(
    modifier: Modifier = Modifier, content: MaterialBarChartScope.() -> Unit
) {
    val scope = remember { MaterialBarChartScope().apply(content) }

    var width by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    val xAxis = remember { StaticSectionedXAxis<CategoryDataPoint>(shiftFraction = 0.5f) }
    val gridAxis = remember { StaticSectionedXAxis<CategoryDataPoint>() }
    val yAxis = remember(scope.dataPoints) {
        StaticYAxis(
            minValue = 0f,
            maxValue = scope.dataPoints.maxOfOrNull { it.value } ?: 0f)
    }

    val renderer = remember(xAxis, yAxis) {
        BarChartRenderer(
            xScale = xAxis, yAxis = yAxis, barWidth = scope.barWidth
        )
    }
    val valueRenderer = remember(xAxis, yAxis) {
        ValuePanelRenderer(yAxis = yAxis)
    }
    val gridRenderer = remember(gridAxis, yAxis) {
        GridPanelRenderer(xAxis = gridAxis, yAxis = yAxis)
    }

    val categoryLabelsRenderer = remember(xAxis, yAxis) {
        CategoryPanelRenderer(xScale = xAxis)
    }

    val output = remember(scope.dataPoints, width, height) {
        xAxis.updateViewWidth(width)
        gridAxis.updateViewWidth(width)
        yAxis.updateViewHeight(height)

        renderer.render(scope.dataPoints)
    }

    val valueOutput = remember(height) {
        valueRenderer.render(scope.valuePanel.valueFormat, scope.valuePanel.ticks)
    }

    val gridOutput = remember(scope.dataPoints, valueOutput) {
        gridRenderer.render(scope.dataPoints, valueOutput)
    }

    val categoryLabelsOutput = remember(scope.dataPoints, valueOutput) {
        categoryLabelsRenderer.render(scope.dataPoints)
    }

    var tapPoint by remember { mutableStateOf(Offset(0f, 0f)) }
    val animatedPoint by animateOffsetAsState(targetValue = tapPoint)

    var selectedIndex by remember { mutableStateOf(-1) }

    Column(modifier = modifier) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.weight(1f)) {
                MaterialGridPanel(gridOutput = gridOutput, gridScope = scope.gridPanel)
                BarChart(
                    modifier = Modifier
                        .onGloballyPositioned {
                            width = it.size.width.toFloat()
                            height = it.size.height.toFloat()
                        }
                        .pointerInput(Unit) {
                            detectTapGestures { tapOffset ->
                                var isSelected = false
                                for ((index, bar) in output.bars.withIndex()) {
                                    if (ChartUtils.isTapInRect(tapOffset.toDomain(), bar.rect)) {
                                        tapPoint = tapOffset
                                        selectedIndex = index
                                        isSelected = true
                                        break
                                    }
                                }
                                if (!isSelected) {
                                    selectedIndex = -1
                                }
                            }
                        }, output = output,
                    scope = scope,
                    selectedIndex = selectedIndex
                )
                selectedIndex.takeIf { it in output.bars.indices }?.let {
                    scope.dataPoints.getOrNull(it)
                }?.let { datapoint ->
                    SimplePopup(
                        modifier = Modifier.offset { animatedPoint.round() },
                        title = datapoint.category,
                        label = datapoint.value.toString(),
                        color = scope.barColor
                    )
                }
            }
            MaterialValuePanel(
                valueOutput = valueOutput,
                scope = scope.valuePanel,
                alignTickToRight = false
            )
        }
        MaterialXAxisComponent(
            output = categoryLabelsOutput,
            scope = scope.xAxisPanel
        )
    }
}

@Composable
private fun BarChart(
    modifier: Modifier,
    output: BarChartOutput,
    scope: MaterialBarChartScope,
    selectedIndex: Int
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    val animatedProgress by animateFloatAsState(
        progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )

    val rectItems = remember(output.bars) {
        output.bars.map { it.rect.toCompose() }
    }
    LaunchedEffect(Unit) { progress = 1f }

    Canvas(
        modifier = modifier
            .background(scope.backgroundColor)
            .fillMaxSize()
            .clipToBounds()
    ) {
        for (index in rectItems.indices) {
            val rect = rectItems[index]
            val animatedHeight = if (animatedProgress == 1f) rect.size.height else rect.size.height * animatedProgress
            val topLeft = if (animatedProgress == 1f) rect.topLeft else rect.topLeft.copy(y = size.height - animatedHeight)
            val size = if (animatedProgress == 1f) rect.size else rect.size.copy(height = animatedHeight)

            drawRect(
                color = scope.barColor.copy(
                    alpha = if (selectedIndex >= 0 && index != selectedIndex) 0.5f else 1f
                ),
                topLeft = topLeft,
                size = size
            )
        }
    }
}