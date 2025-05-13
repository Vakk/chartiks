package com.valeriik.compose.material.panels.legend

import androidx.compose.ui.text.TextStyle
import com.valeriik.chart.compose.model.Legend

class MaterialLegendPanelScope {
    internal var textStyle: TextStyle = TextStyle.Default
    internal var type: Type = Type.Line
    internal var spacing: Float = 8f

    sealed interface Type {
        object Line : Type
        object Circle : Type
    }
}