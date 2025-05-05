package com.valeriik.compose.material.panels.value

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.valeriik.chart.core.renderers.ValueOutput
import kotlin.math.max

@Composable
internal fun MaterialValuePanel(
    modifier: Modifier = Modifier,
    valueOutput: ValueOutput,
    scope: MaterialValuePanelScope,
    alignTickToRight: Boolean
) {
    val measurer = rememberTextMeasurer()
    val tickLineSize = with(LocalDensity.current) {
        scope.tickLineSize.toDp()
    }

    val tickWidth = with(LocalDensity.current) {
        scope.tickWidth.toDp()
    }

    val spacing = with(LocalDensity.current) {
        scope.innerSpacing.toDp()
    }

    var maxWidth by remember { mutableStateOf(0f) }
    val valueSizes = remember(valueOutput) {
        var maxW = Float.MIN_VALUE
        val result = valueOutput.labels.map {
            val size = measurer.measure(it.text, scope.textStyle).size
            maxW = max(size.width.toFloat(), maxW)
            size
        }
        maxWidth = maxW
        result
    }

    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .width(with(LocalDensity.current) {
                maxWidth.toDp() + tickLineSize * 2 + spacing * 2
            })
    ) {
        val canvasWidth = size.width
        val middlePoint = canvasWidth.div(2f)
        val leftSpacing = if (alignTickToRight) {
            0f
        } else {
            tickWidth.toPx()
        }
        for (i in valueOutput.labels.indices) {
            val label = valueOutput.labels[i]
            val size = valueSizes[i]
            val drawPoint =
                Offset(
                    middlePoint - size.width.div(2f) + leftSpacing,
                    label.position.y - size.height.div(2f)
                )
            drawText(
                textMeasurer = measurer,
                text = label.text,
                style = scope.textStyle,
                topLeft = drawPoint
            )
            if (alignTickToRight) {
                drawLine(
                    scope.tickColor,
                    start = Offset(canvasWidth - tickLineSize.toPx(), label.position.y),
                    end = Offset(canvasWidth, label.position.y),
                    strokeWidth = tickWidth.toPx()
                )
            } else {
                drawLine(
                    scope.tickColor,
                    start = Offset(0f, label.position.y),
                    end = Offset(tickLineSize.toPx(), label.position.y),
                    strokeWidth = tickWidth.toPx()
                )
            }
        }
    }
}