package com.valeriik.chart.examples.ui.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.valeriik.chart.compose.ChartComponent
import com.valeriik.chart.compose.panel.XAxisScope
import com.valeriik.chart.compose.panel.grid.GridMode
import com.valeriik.chart.core.data.CartesianDataPoint
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun StackedLineChartComponent(
    modifier: Modifier = Modifier
) {
    val dataPoints1 = listOf(
        CartesianDataPoint(0f, Date(124, 9, 1).time),
        CartesianDataPoint(1.1f, Date(124, 9, 3).time),
        CartesianDataPoint(1.5f, Date(124, 9, 7).time),
        CartesianDataPoint(0.8f, Date(124, 9, 10).time),
        CartesianDataPoint(2.8f, Date(124, 9, 14).time),
        CartesianDataPoint(3.8f, Date(124, 9, 20).time),
        CartesianDataPoint(2.8f, Date(124, 9, 24).time),
        CartesianDataPoint(3.6f, Date(124, 9, 28).time),
        CartesianDataPoint(2f, Date(124, 9, 30).time),
    ).sortedBy { it.timeStamp }

    val dataPoints2 = listOf(
        CartesianDataPoint(1.8f, Date(124, 9, 1).time),
        CartesianDataPoint(2.5f, Date(124, 9, 3).time),
        CartesianDataPoint(2f, Date(124, 9, 7).time),
        CartesianDataPoint(4f, Date(124, 9, 14).time),
        CartesianDataPoint(1.5f, Date(124, 9, 20).time),
        CartesianDataPoint(0.5f, Date(124, 9, 24).time),
        CartesianDataPoint(1.8f, Date(124, 9, 28).time),
        CartesianDataPoint(3.8f, Date(124, 9, 30).time),
    ).sortedBy { it.timeStamp }

    ChartComponent(
        modifier = modifier.fillMaxSize(),
    ) {
        shareValueScale = false

        useGrid {
            verticalMode = GridMode.None
            color = Color.LightGray
            horizontalMode = GridMode.Stroke(strokeDp = 1.dp)
        }

        xAxis = XAxisScope.DateScope {
            fromTimestamp = dataPoints1.first().timeStamp
            toTimestamp = dataPoints1.last().timeStamp
        }

        data("data1", dataPoints1)
        data("data2", dataPoints2)

        valuePanel("data1") {
            valueFormat = "%.1fk"
            style = TextStyle.Default.copy(color = Color.Red)
            horizontalSpacingDp = 8.dp
            backgroundColor = Color.Red
            verticalArrangement = Arrangement.spacedBy(8.dp)
        }

        datePanel("data1") {
            dateFormat = SimpleDateFormat("dd MMM")
            textStyle = TextStyle.Default.copy(Color.Gray)
            verticalSpacingDp = 16.dp
        }

        lineChart("data1") {
            color = Color.Magenta
            circleRadius = 20f
        }

        lineChart("data2") {
            color = Color.Blue
            circleRadius = 20f
        }
    }
}