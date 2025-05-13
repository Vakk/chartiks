package com.valeriik.compose.material.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import com.valeriik.chart.core.renderers.LabelsOutput
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlin.math.max

@Composable
fun MaterialXAxisComponent(
    modifier: Modifier = Modifier,
    output: LabelsOutput,
    scope: MaterialXAxisScope
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

    var maxHeight by remember { mutableStateOf(0f) }
    val valueSizes = remember(output) {
        var maxH = Float.MIN_VALUE
        val result = output.labels.map {
            val size = measurer.measure(it.text, scope.textStyle).size
            maxH = max(size.height.toFloat(), maxH)
            size
        }
        maxHeight = maxH
        result
    }

    Canvas(
        modifier = modifier
            .height(with(LocalDensity.current) {
                maxHeight.toDp() + tickLineSize * 2 + spacing * 2
            })
            .fillMaxWidth()
    ) {
        var eX = 0f
        var sX: Float
        var maxWidth: Float = 0f

        for (i in output.labels.indices) {
            val label = output.labels[i]

            sX = label.position.x - valueSizes[i].width.div(2f)

            if (sX < eX) {
                sX = eX + spacing.toPx()
            }
            if (sX > label.position.x &&  label.position.x != 0f) {
                continue
            }
            maxWidth = if (sX + valueSizes[i].width > size.width) {
                size.width - sX
            } else {
                valueSizes[i].width.toFloat()
            }

            if (maxWidth < valueSizes[i].width.div(2f)) {
                continue
            }

            drawText(
                textMeasurer = measurer,
                text = label.text,
                style = scope.textStyle,
                topLeft = Offset(
                    sX, label.position.y + tickLineSize.toPx() + spacing.toPx()
                ),
                overflow = TextOverflow.Ellipsis,
                size = Size(maxWidth, valueSizes[i].height.toFloat())
            )
            drawLine(
                color = scope.tickColor,
                start = Offset(label.position.x, 0f),
                end = Offset(label.position.x, tickLineSize.toPx()),
                strokeWidth = tickWidth.toPx()
            )

            eX = sX + valueSizes[i].width
        }
    }
}

class MaterialXAxisScope {
    var dateFormatter: DateFormat = SimpleDateFormat.getDateInstance()
    var textStyle = TextStyle.Default.copy(Color.DarkGray)
    var tickLineSize = 24f
    var tickWidth = 1f
    var tickColor: Color = Color.LightGray
    var innerSpacing = 16f
}