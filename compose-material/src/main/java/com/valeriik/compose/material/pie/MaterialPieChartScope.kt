package com.valeriik.compose.material.pie

import androidx.compose.ui.graphics.Color
import com.valeriik.chart.core.data.CategoryDataPoint

class MaterialPieChartScope {
    internal var dataPoints: List<CategoryDataPoint> = emptyList()
    internal var categoryColors: Map<String, Color> = emptyMap()

    var selectionOffset: Float = 8f

    var borderColor: Color = Color.White
    var borderThickness: Float = 4f

    var backgroundColor: Color = Color.LightGray


    fun dataPoints(datapoints: List<CategoryDataPoint>) {
        dataPoints = datapoints
    }

    fun category(label: String, color: Color) {
        categoryColors += label to color
    }
}

