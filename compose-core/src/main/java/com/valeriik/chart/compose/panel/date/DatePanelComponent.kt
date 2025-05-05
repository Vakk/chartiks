package com.valeriik.chart.compose.panel.date

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import com.valeriik.chart.core.renderers.LabelsOutput

@Composable
fun DatePanelComponent(
    modifier: Modifier = Modifier,
    dateOutput: LabelsOutput,
    style: TextStyle,
    arrangement: Arrangement.Horizontal
) {
    val measurer = rememberTextMeasurer()
    var height by remember { mutableFloatStateOf(0f) }
    val labelWidth = remember(dateOutput.labels) {
        var maxHeight = 0f
        dateOutput.labels.map {
            val result = measurer.measure(it.text, style = style)
            if (maxHeight < result.size.height) {
                maxHeight = result.size.height.toFloat()
            }
            result.size.width
        }.also {
            height = maxHeight
        }
    }

    val density = LocalDensity.current

    val arrangementSpacing = remember {
        with(density) {
            arrangement.spacing.toPx()
        }
    }
    Canvas(
        modifier = modifier
            .height(
                with(LocalDensity.current) {
                    height.toDp()
                })
    ) {
        var lastX = -arrangementSpacing
        for (i in dateOutput.labels.indices) {
            val label = dateOutput.labels[i]
            val sX = label.position.x - labelWidth[i].div(2f)
            if (lastX != 0f && sX < lastX + arrangementSpacing) {
                continue
            }
            lastX = sX + labelWidth[i]

            if (lastX < size.width) {
                drawText(
                    textMeasurer = measurer,
                    text = label.text,
                    style = style,
                    topLeft = Offset(sX, 0f),
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
        }
    }
}