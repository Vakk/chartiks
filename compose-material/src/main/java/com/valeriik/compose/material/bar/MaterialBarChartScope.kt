package com.valeriik.compose.material.bar

import androidx.compose.ui.graphics.Color
import com.valeriik.chart.core.data.CategoryDataPoint
import com.valeriik.compose.material.components.MaterialPopupScope
import com.valeriik.compose.material.components.MaterialXAxisScope
import com.valeriik.compose.material.panels.grid.MaterialGridScope
import com.valeriik.compose.material.panels.value.MaterialValuePanelScope

class MaterialBarChartScope {
    var backgroundColor: Color = Color.Transparent
    var barWidth: Float = 20f
    var barColor: Color = Color.Blue
    internal var valuePanel: MaterialValuePanelScope = MaterialValuePanelScope()
    internal var xAxisPanel: MaterialXAxisScope = MaterialXAxisScope()
    internal var gridPanel: MaterialGridScope = MaterialGridScope()

    internal var dataPoints = emptyList<CategoryDataPoint>()
    fun dataPoints(dataPoints: List<CategoryDataPoint>) {
        this.dataPoints += dataPoints
    }

    fun valuePanel(content: MaterialValuePanelScope.() -> Unit) {
        valuePanel.apply(content)
    }

    fun xAxis(content: MaterialXAxisScope.() -> Unit) {
        xAxisPanel.apply(content)
    }

    fun gridPanel(content: MaterialGridScope.() -> Unit) {
        gridPanel.apply(content)
    }
}