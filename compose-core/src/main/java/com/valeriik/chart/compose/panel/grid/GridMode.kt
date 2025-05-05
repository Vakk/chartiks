package com.valeriik.chart.compose.panel.grid

import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp

sealed class GridMode(val strokeDp: Dp) {
    class Stroke(strokeDp: Dp) : GridMode(strokeDp)
    class Dashed(strokeDp: Dp, val pathEffect: PathEffect) : GridMode(strokeDp)
    data object None : GridMode(Dp.Hairline)
}