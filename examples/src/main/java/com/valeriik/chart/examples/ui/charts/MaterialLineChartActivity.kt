package com.valeriik.chart.examples.ui.charts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.chart.examples.ui.theme.BudgettingassignmentTheme
import com.valeriik.compose.material.line.MaterialLineChart
import java.text.SimpleDateFormat
import java.util.Date

class MaterialLineChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BudgettingassignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MaterialLineChart(Modifier.padding(innerPadding)) {
                        data = listOf(
                            CartesianDataPoint(value = 12f, timeStamp = Date(124, 9, 1).time),
                            CartesianDataPoint(value = 19f, timeStamp = Date(124, 9, 2).time),
                            CartesianDataPoint(value = 3f, timeStamp = Date(124, 9, 3).time),
                            CartesianDataPoint(value = 5f, timeStamp = Date(124, 9, 4).time),
                            CartesianDataPoint(value = 2f, timeStamp = Date(124, 9, 5).time),
                            CartesianDataPoint(value = 3f, timeStamp = Date(124, 9, 6).time)
                        )

                        valuePanel {

                        }

                        datePanel {
                            dateFormatter = SimpleDateFormat.getDateInstance()
                        }
                    }
                }
            }
        }
    }
}