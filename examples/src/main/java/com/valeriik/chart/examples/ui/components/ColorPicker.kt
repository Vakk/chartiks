package com.valeriik.chart.examples.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row {
        colors.forEach { color ->
            Box(modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                .background(color, shape = CircleShape)
                .border(
                    2.dp,
                    if (color == selectedColor) {
                        if (color == Color.Black) {
                            Color.Gray
                        } else {
                            Color.Black
                        }
                    } else {
                        if (color == Color.White) Color.Gray else Color.Transparent
                    },
                    CircleShape
                )
                .clickable { onColorSelected(color) })
        }
    }
}
