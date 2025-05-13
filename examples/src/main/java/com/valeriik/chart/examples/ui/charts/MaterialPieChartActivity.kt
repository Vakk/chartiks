package com.valeriik.chart.examples.ui.charts

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.valeriik.chart.core.data.CategoryDataPoint
import com.valeriik.chart.examples.ui.components.ColorPicker
import com.valeriik.chart.examples.ui.theme.BudgettingassignmentTheme
import com.valeriik.compose.material.pie.MaterialPieChart

@OptIn(ExperimentalMaterial3Api::class)
class MaterialPieChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgettingassignmentTheme {
                val exampleDataPoints = remember {
                    listOf(
                        CategoryDataPoint(
                            value = 30f,
                            category = "Datapoint1",
                        ),
                        CategoryDataPoint(
                            value = 50f, category = "Datapoint2"
                        ),
                        CategoryDataPoint(
                            value = 16f,
                            category = "Datapoint3",
                        ),
                        CategoryDataPoint(
                            value = 28f,
                            category = "Datapoint4",
                        ),
                        CategoryDataPoint(
                            value = 30f, category = "Datapoint5"
                        ),
                        CategoryDataPoint(
                            value = 3f,
                            category = "Datapoint6",
                        ),
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = { Text("Pie Chart") }, navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    })
                }) { innerPadding ->

                    val exampleColors = mutableMapOf(
                            "Datapoint1" to MaterialTheme.colorScheme.primary,
                            "Datapoint2" to MaterialTheme.colorScheme.secondary,
                            "Datapoint3" to MaterialTheme.colorScheme.tertiary,
                            "Datapoint4" to MaterialTheme.colorScheme.surfaceVariant,
                            "Datapoint5" to MaterialTheme.colorScheme.surface,
                            "Datapoint6" to MaterialTheme.colorScheme.error,
                        )
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            var background by remember { mutableStateOf(Color.LightGray) }
                            val selectedDataPointChips = remember { mutableStateListOf<String>() }
                            val activeDataPoints =
                                selectedDataPointChips.mapNotNull { selectedCategory -> exampleDataPoints.find { it.category == selectedCategory } }
                            var chartSectionBorderColor by remember { mutableStateOf(Color.White) }
                            var chartSectionBorderSize by remember { mutableStateOf(20f) }
                            var chartSectionOffset by remember { mutableStateOf(20f) }

                            MaterialPieChart(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally)
                                    .clipToBounds()
                            ) {
                                backgroundColor = background
                                dataPoints(activeDataPoints)

                                borderColor = chartSectionBorderColor
                                borderThickness = chartSectionBorderSize

                                selectionOffset = chartSectionOffset

                                activeDataPoints.forEach {
                                    category(it.category, exampleColors[it.category]!!)
                                }
                            }

                            SettingsPanel(
                                modifier = Modifier.weight(1f),
                                color = background,
                                onColor = { background = it },
                                chartSectionBorderColor = chartSectionBorderColor,
                                onChartSectionBorderColor = { chartSectionBorderColor = it },
                                chartSectionBorderSize = chartSectionBorderSize,
                                onChartSectionBorderSize = { chartSectionBorderSize = it },
                                chartSectionOffset = chartSectionOffset,
                                onChartSectionOffset = { chartSectionOffset = it },
                                exampleDataPoints = exampleDataPoints,
                                selectedDataPointChips = selectedDataPointChips,
                                exampleColors = exampleColors
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            var background by remember { mutableStateOf(Color.LightGray) }
                            val selectedDataPointChips = remember { mutableStateListOf<String>() }
                            val activeDataPoints =
                                selectedDataPointChips.mapNotNull { selectedCategory -> exampleDataPoints.find { it.category == selectedCategory } }
                            var chartSectionBorderColor by remember { mutableStateOf(Color.White) }
                            var chartSectionBorderSize by remember { mutableStateOf(20f) }
                            var chartSectionOffset by remember { mutableStateOf(20f) }

                            MaterialPieChart(
                                modifier = Modifier.weight(1f)
                            ) {
                                backgroundColor = background
                                dataPoints(activeDataPoints)

                                borderColor = chartSectionBorderColor
                                borderThickness = chartSectionBorderSize

                                selectionOffset = chartSectionOffset

                                activeDataPoints.forEach {
                                    category(it.category, exampleColors[it.category]!!)
                                }
                            }

                            SettingsPanel(
                                modifier = Modifier.weight(1f),
                                color = background,
                                onColor = { background = it },
                                chartSectionBorderColor = chartSectionBorderColor,
                                onChartSectionBorderColor = { chartSectionBorderColor = it },
                                chartSectionBorderSize = chartSectionBorderSize,
                                onChartSectionBorderSize = { chartSectionBorderSize = it },
                                chartSectionOffset = chartSectionOffset,
                                onChartSectionOffset = { chartSectionOffset = it },
                                exampleDataPoints = exampleDataPoints,
                                selectedDataPointChips = selectedDataPointChips,
                                exampleColors = exampleColors
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsPanel(
    modifier: Modifier,
    color: Color,
    onColor: (Color) -> Unit,
    chartSectionBorderColor: Color,
    onChartSectionBorderColor: (Color) -> Unit,
    chartSectionBorderSize: Float,
    onChartSectionBorderSize: (Float) -> Unit,
    chartSectionOffset: Float,
    onChartSectionOffset: (Float) -> Unit,
    exampleDataPoints: List<CategoryDataPoint>,
    selectedDataPointChips: MutableList<String>,
    exampleColors: Map<String, Color>,
) {
    Column(modifier = modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        Text(
            text = "Pie Chart Settings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = "Background Color")
        ColorPicker(
            selectedColor = color,
            colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer, Color.LightGray),
            onColorSelected = { onColor(it) })
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Data points")
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            exampleDataPoints.forEach { categoryDataPoint ->
                InputChip(
                    selected = selectedDataPointChips.contains(categoryDataPoint.category),
                    onClick = {
                        if (!selectedDataPointChips.contains(categoryDataPoint.category)) {
                            selectedDataPointChips.add(categoryDataPoint.category)
                        } else {
                            selectedDataPointChips.remove(categoryDataPoint.category)
                        }
                    },
                    label = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    exampleColors[categoryDataPoint.category]!!,
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                        Text("${categoryDataPoint.category} (${categoryDataPoint.value})")
                    }
                )
            }
        }
        // Selection Shift Slider
        Text(text = "Chart section border color")
        ColorPicker(
            colors = listOf(
                Color.White,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.tertiary
            ), selectedColor = chartSectionBorderColor,
            onColorSelected = onChartSectionBorderColor
        )
        // Selection Shift Slider
        Text(text = "Chart section width (dp)")
        Slider(
            value = chartSectionBorderSize,
            onValueChange = onChartSectionBorderSize,
            valueRange = 0f..40f
        )
        // Selection Offset
        Text(text = "Selection Offset (dp)")
        Slider(
            value = chartSectionOffset,
            onValueChange = onChartSectionOffset,
            valueRange = 0f..40f
        )
    }
}