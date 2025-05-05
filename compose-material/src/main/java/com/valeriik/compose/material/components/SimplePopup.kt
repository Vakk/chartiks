package com.valeriik.compose.material.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valeriik.chart.core.data.CartesianDataPoint
import com.valeriik.chart.core.data.DataPoint
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun SimplePopup(
    modifier: Modifier,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Black.copy(0.7f),
            contentColor = Color.White
        ),
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            title()
            content()
        }
    }
}

@Composable
fun SimplePopup(
    modifier: Modifier,
    title: String,
    label: String,
) {
    SimplePopup(modifier = modifier,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        content = {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    )
}


@Composable
fun SimplePopup(
    modifier: Modifier,
    title: String,
    label: String,
    color: Color,
) {
    SimplePopup(modifier = modifier, title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                drawCircle(color = color)
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
    }, content = {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    })
}


@Composable
fun SimplePopup(
    modifier: Modifier,
    dataPoint: CartesianDataPoint
) {
    val title = remember(dataPoint) {
        val date = Date(dataPoint.timeStamp)
        val dateFormat = SimpleDateFormat("MMMM d")
        dateFormat.format(date)
    }
    SimplePopup(
        modifier = modifier,
        title = title,
        label = "Value: ${dataPoint.value}"
    )
}


@Composable
fun SimplePopup(
    modifier: Modifier,
    dataPoint: DataPoint,
    title: String,
    color: Color,
) {
    SimplePopup(
        modifier = modifier,
        title = title,
        label = dataPoint.value.toString(),
        color = color
    )
}