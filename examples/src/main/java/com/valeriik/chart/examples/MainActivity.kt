package com.valeriik.chart.examples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valeriik.chart.examples.ui.charts.MaterialBarChartActivity
import com.valeriik.chart.examples.ui.charts.MaterialLineChartActivity
import com.valeriik.chart.examples.ui.charts.MaterialPieChartActivity
import com.valeriik.chart.examples.ui.theme.BudgettingassignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BudgettingassignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            listOf(
                                ChartCardData(
                                    "Pie Chart",
                                    MaterialPieChartActivity::class.java
                                ),
                                ChartCardData(
                                    "Line Chart",
                                    MaterialLineChartActivity::class.java
                                ),
                                ChartCardData(
                                    "Bar Chart",
                                    MaterialBarChartActivity::class.java
                                )
                            )
                        ) {
                            ChartCard(it) {
                                navigateTo(it.activityClass)
                            }
                        }
                    }
                }
            }
        }
    }

    fun navigateTo(activityClass: Class<out Activity>) {
        startActivity(Intent(this, activityClass))
    }
}

@Composable
private fun ChartCard(data: ChartCardData, onClick: () -> Unit) {
    Card(modifier = Modifier.aspectRatio(1f), onClick = onClick) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                text = data.title
            )
        }
    }
}

private data class ChartCardData(
    val title: String, val activityClass: Class<out Activity>
)