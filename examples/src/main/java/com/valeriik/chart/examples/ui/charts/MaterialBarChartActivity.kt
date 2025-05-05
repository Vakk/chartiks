package com.valeriik.chart.examples.ui.charts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.valeriik.chart.core.data.CategoryDataPoint
import com.valeriik.chart.examples.ui.theme.BudgettingassignmentTheme
import com.valeriik.compose.material.bar.MaterialBarChart

class MaterialBarChartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BudgettingassignmentTheme {
                Scaffold { innerPadding ->
                    MaterialBarChart(modifier = Modifier.padding(innerPadding)) {
                        barWidth = 60f
                        dataPoints(
                            listOf(
                                CategoryDataPoint(1f, "Alpha"),
                                CategoryDataPoint(2f, "Beta"),
                                CategoryDataPoint(3f, "Gama"),
                                CategoryDataPoint(4f, "Delta"),
                            )
                        )
                        valuePanel {
                            valueFormat = "%.1f"
                        }
                    }
                }
            }
        }
    }
}