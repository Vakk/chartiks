package com.valeriik.chart.compose.utils

import androidx.compose.ui.geometry.Offset
import com.valeriik.chart.core.data.Rect
import com.valeriik.chart.core.data.Size

fun Offset.toDomain(): com.valeriik.chart.core.data.Offset {
    return com.valeriik.chart.core.data.Offset(
        x = x, y = y
    )
}

fun com.valeriik.chart.core.data.Offset.toCompose(): Offset {
    return Offset(
        x = x, y = y
    )
}

fun androidx.compose.ui.geometry.Size.toDomain(): Size {
    return Size(
        width = width, height = height
    )
}

fun Size.toCompose(): androidx.compose.ui.geometry.Size {
    return androidx.compose.ui.geometry.Size(
        width = width, height = height
    )

}

fun androidx.compose.ui.unit.IntSize.toDomain(): Size {
    return Size(
        width = width.toFloat(), height = height.toFloat()
    )
}

fun Rect.toCompose(): androidx.compose.ui.geometry.Rect {
    return androidx.compose.ui.geometry.Rect(
        left = topLeft.x,
        top = topLeft.y,
        right = topLeft.x + size.width,
        bottom = topLeft.y + size.height
    )
}

fun androidx.compose.ui.geometry.Rect.toDomain(): Rect {
    return Rect(
        left = topLeft.x,
        top = topLeft.y,
        right = topLeft.x + size.width,
        bottom = topLeft.y + size.height
    )
}