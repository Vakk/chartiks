package com.valeriik.compose.material.pie

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.valeriik.chart.compose.utils.toDomain
import com.valeriik.chart.core.renderers.PieChartOutput
import com.valeriik.chart.core.renderers.PieChartRenderer
import com.valeriik.chart.core.renderers.PieSegment
import com.valeriik.chart.core.utils.ChartUtils
import com.valeriik.compose.material.components.SimplePopup
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MaterialPieChart(
    modifier: Modifier = Modifier, content: MaterialPieChartScope.() -> Unit
) {
    val scope = remember { MaterialPieChartScope() }
    scope.apply(content)
    val renderer = remember { PieChartRenderer() }
    val output = remember(scope.dataPoints) { renderer.render(scope.dataPoints) }

    var selectedIndex by remember { mutableStateOf(-1) }
    var tapPosition by remember { mutableStateOf<Offset?>(null) }
    var selectedSection by remember { mutableStateOf<PieSegment?>(null) }

    var appearanceProgress by remember { mutableStateOf(0f) }
    val animatedAppearanceProgress by animateFloatAsState(
        targetValue = appearanceProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )

    var selectionShiftProgress by remember { mutableFloatStateOf(0f) }
    val animatedSelectionShiftProgress by animateFloatAsState(
        targetValue = selectionShiftProgress, animationSpec = tween(300)
    )

    val shiftOffset = with(LocalDensity.current) {
        scope.selectionOffset.dp.toPx()
    }

    LaunchedEffect(Unit) {
        appearanceProgress = 1f
    }

    LaunchedEffect(scope.dataPoints) {
        selectedIndex = -1
        selectedSection = null
    }

    LaunchedEffect(selectedIndex) {
        val isDeselected = selectedSection != null
        selectionShiftProgress = 0f
        if (isDeselected) {
            delay(200)
        }
        selectedSection = output.segments.getOrNull(selectedIndex)
        if (selectedSection != null) {
            //avoid moving progress when nothing else was selected.
            selectionShiftProgress = 1f
        }
    }

    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier) {
        Canvas(modifier = Modifier
            .aspectRatio(1f)
            .pointerInput(scope.dataPoints) {
                detectTapGestures {
                    selectedIndex = detectSegmentOnTap(
                        tapOffset = it.toDomain(),
                        size = size.toDomain(),
                        output = output
                    )
                    tapPosition = it
                }
            }) {
            drawChart(
                textMeasurer = textMeasurer,
                scope = scope,
                output = output,
                selectedSection = selectedSection,
                appearanceProgress = animatedAppearanceProgress,
                shiftValue = shiftOffset,
                selectionShiftProgress = animatedSelectionShiftProgress,
                colors = scope.categoryColors
            )
        }
        selectedIndex.takeIf { it in output.segments.indices }?.let {
            scope.dataPoints[it]
        }?.let {
            SimplePopup(
                modifier = Modifier.offset {
                    IntOffset(tapPosition?.x?.toInt() ?: 0, tapPosition?.y?.toInt() ?: 0)
                }, dataPoint = it,
                title = it.category,
                color = scope.categoryColors[it.category] ?: Color.Transparent
            )
        }
    }
}

private fun DrawScope.drawChart(
    textMeasurer: TextMeasurer,
    scope: MaterialPieChartScope,
    output: PieChartOutput,
    selectedSection: PieSegment?,
    appearanceProgress: Float,
    shiftValue: Float,
    selectionShiftProgress: Float,
    colors: Map<String, Color>
) {
    val radius = size.minDimension / 2f
    val selectionShiftOffset = shiftValue * selectionShiftProgress

    val circleBounds = Rect(center = center, radius = radius)

    drawCircle(scope.backgroundColor)

    for (index in output.segments.indices) {
        val segment = output.segments[index]
        val dataPoint = segment.dataPoint
        val isSelected = selectedSection == segment

        drawArc(
            color = colors[dataPoint.category]?.copy(alpha = if (!isSelected) 1f - 0.5f * selectionShiftProgress else 1f)
                ?: continue,
            startAngle = segment.startAngle.times(appearanceProgress),
            sweepAngle = segment.sweepAngle.times(appearanceProgress),
            useCenter = true,
            style = Fill,
            topLeft = if (isSelected) {
                circleBounds.topLeft + calculateOffset(
                    startAngle = segment.startAngle,
                    sweepAngle = segment.sweepAngle,
                    shiftOffset = selectionShiftOffset
                )
            } else {
                circleBounds.topLeft
            },
            size = circleBounds.size,
        )
        if (scope.borderThickness > 0f && scope.borderColor != Color.Transparent) {
            drawArc(
                color = scope.borderColor,
                startAngle = segment.startAngle.times(appearanceProgress),
                sweepAngle = segment.sweepAngle.times(appearanceProgress),
                useCenter = true,
                style = Stroke(width = scope.borderThickness),  // Adjust width for desired thickness
                topLeft = if (isSelected) {
                    circleBounds.topLeft + calculateOffset(
                        startAngle = segment.startAngle,
                        sweepAngle = segment.sweepAngle,
                        shiftOffset = selectionShiftOffset
                    )
                } else {
                    circleBounds.topLeft
                },
                size = circleBounds.size,
            )
        }
    }
}

private fun detectSegmentOnTap(
    tapOffset: com.valeriik.chart.core.data.Offset,
    size: com.valeriik.chart.core.data.Size,
    output: PieChartOutput
): Int {
    for (index in output.segments.indices) {
        val segment = output.segments[index]
        if (ChartUtils.isTapOnPieSegment(
                tapOffset = tapOffset,
                size = size,
                startAngle = segment.startAngle,
                sweepAngle = segment.sweepAngle
            )
        ) {
            return index
        }
    }
    return -1
}

private fun calculateOffset(
    startAngle: Float,
    sweepAngle: Float,
    shiftOffset: Float,
): Offset {
    val angleInRadians =
        calculateShiftOnSelection(startAngle, sweepAngle)
    val shiftX = cos(angleInRadians).times(shiftOffset).toFloat()
    val shiftY = sin(angleInRadians).times(shiftOffset).toFloat()
    return Offset(shiftX, shiftY)
}

private fun calculateShiftOnSelection(
    startAngle: Float,
    sweepAngle: Float
): Double {
    // Calculate the center angle of the segment
    val centerAngle = startAngle.toDouble() + sweepAngle.toDouble() / 2

    // Convert the center angle to radians for trigonometric functions
    return Math.toRadians(centerAngle)
}