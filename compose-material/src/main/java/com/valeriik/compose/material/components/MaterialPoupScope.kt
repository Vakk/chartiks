package com.valeriik.compose.material.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.chart.core.data.Offset


class MaterialPopupScope {
    var content: @Composable (
        Modifier, Offset, CartesianDataPoint
    ) -> Unit = { modifier, _, dataPoint ->
        SimplePopup(modifier = modifier, dataPoint = dataPoint)
    }
}