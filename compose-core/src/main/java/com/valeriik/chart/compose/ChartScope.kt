package com.valeriik.chart.compose

import androidx.compose.ui.graphics.Color
import com.valeriik.chart.compose.panel.date.DatePanelScope
import com.valeriik.chart.compose.panel.grid.GridScope
import com.valeriik.chart.compose.chart.line.LinePlotScope
import com.valeriik.chart.compose.chart.mountain.MountainLinePlotScope
import com.valeriik.chart.compose.panel.XAxisScope
import com.valeriik.chart.compose.panel.YAxisScope
import com.valeriik.chart.compose.panel.value.ValuePanelScope
import com.valeriik.chart.core.data.CartesianDataPoint

class ChartScope {
    var backgroundColor: Color = Color.White

    var shareValueScale: Boolean = false

    var yAxis: YAxisScope = YAxisScope()
    var xAxis: XAxisScope = XAxisScope.IndexScope()
    internal var valuePanels: MutableMap<String, ValuePanelScope> = mutableMapOf()
    internal var datePanels: MutableMap<String, DatePanelScope> = mutableMapOf()
    internal var plots: MutableMap<String, PlotScope> = mutableMapOf()

    internal var dataPoints: MutableMap<String, List<CartesianDataPoint>> = mutableMapOf()

    internal var gridScope: GridScope? = null

    fun lineChart(dataKey: String, content: LinePlotScope.() -> Unit) {
        plots[dataKey] = LinePlotScope().apply(content)
    }

    fun mountainChart(dataKey: String, content: MountainLinePlotScope.() -> Unit) {
        plots[dataKey] = MountainLinePlotScope().apply(content)
    }

    fun valuePanel(dataKey: String, content: ValuePanelScope.() -> Unit) {
        val scope = ValuePanelScope().apply(content)
        valuePanels[dataKey] = scope
    }

    fun datePanel(dataKey: String, content: DatePanelScope.() -> Unit) {
        val scope = DatePanelScope().apply(content)
        datePanels[dataKey] = scope
    }

    fun data(dataKey: String, dataPoints: List<CartesianDataPoint>) {
        this.dataPoints[dataKey] = dataPoints
    }

    fun useGrid(content: GridScope.() -> Unit) {
        gridScope = GridScope().apply(content)
    }
}

interface PlotScope