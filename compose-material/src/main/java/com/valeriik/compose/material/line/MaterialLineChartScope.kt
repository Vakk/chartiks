package com.valeriik.compose.material.line

import androidx.compose.ui.graphics.Color
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.compose.material.components.MaterialPopupScope
import com.valeriik.compose.material.components.MaterialXAxisScope
import com.valeriik.compose.material.panels.grid.MaterialGridScope
import com.valeriik.compose.material.panels.value.MaterialValuePanelScope

class MaterialLineChartScope {
    var lineColor = Color(0xFF6C00F8)
    var lineWidth = 15f

    internal var valuePanelScope = MaterialValuePanelScope()
    internal var datePanelScope = MaterialXAxisScope()
    internal var gridPanelScope = MaterialGridScope()
    internal var materialPopupScope = MaterialPopupScope()

    internal val fillColor get() = lineColor.copy(alpha = 0.15f)

    var data: List<CartesianDataPoint> = emptyList()

    fun valuePanel(block: MaterialValuePanelScope.() -> Unit) {
        valuePanelScope.apply(block)
    }

    fun datePanel(block: MaterialXAxisScope.() -> Unit) {
        datePanelScope.apply(block)
    }

    fun grid(block: MaterialGridScope.() -> Unit) {
        gridPanelScope.apply(block)
    }

    fun popup(block: MaterialPopupScope.() -> Unit) {
        materialPopupScope.apply(block)
    }
}