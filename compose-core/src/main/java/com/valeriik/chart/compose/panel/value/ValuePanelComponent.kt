package com.valeriik.chart.compose.panel.value

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.valeriik.chart.core.renderers.ValueOutput

@Composable
fun ValuePanelComponent(
    modifier: Modifier = Modifier,
    output: ValueOutput,
    style: TextStyle,
    background: Color,
    arrangement: Arrangement.Vertical,
) {
    val textMeasurer = rememberTextMeasurer()
    val (width, height) = remember(output.labels) {
        var maxWidth = 0f
        var maxHeight = 0f
        for (label in output.labels) {
            val size = textMeasurer.measure(label.text, style = style)
            maxWidth = maxWidth.coerceAtLeast(size.size.width.toFloat())
            maxHeight = maxHeight.coerceAtLeast(size.size.height.toFloat())
        }
        maxWidth to maxHeight
    }
    Canvas(modifier = Modifier
        .then(modifier)
        .width(
            with(LocalDensity.current) { width.toDp() }
        )) {
        output.labels.forEach {
            if (0f <= it.position.x && it.position.x < size.width &&
                0f <= it.position.y && it.position.y < size.height
            ) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = it.text,
                    style = style,
                    topLeft = Offset(it.position.x, it.position.y - height.div(2f))
                )
            }
        }
    }
}