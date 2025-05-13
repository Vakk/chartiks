package com.valeriik.compose.material.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.valeriik.chart.compose.model.ColoredChartDataPoint
import com.valeriik.chart.core.data.CartesianDataPoint
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class SimplePopupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun simplePopup_titleAndLabel_displaysCorrectly() {
        composeTestRule.setContent {
            SimplePopup(
                modifier = Modifier,
                title = "Test Title",
                label = "Test Label"
            )
        }

        composeTestRule.onNodeWithText("Test Title").assertExists()
        composeTestRule.onNodeWithText("Test Label").assertExists()
    }

    @Test
    fun simplePopup_cartesianDatapoint_displaysCorrectly() {
        val dataPoint = CartesianDataPoint(0f, Date().time)
        composeTestRule.setContent {
            SimplePopup(
                modifier = Modifier,
                dataPoint = dataPoint
            )
        }

        val expectedTitle = SimpleDateFormat("MMMM d").format(Date(dataPoint.timeStamp))
        val expectedLabel = "Value: ${dataPoint.value}"

        composeTestRule.onNodeWithText(expectedTitle).assertExists()
        composeTestRule.onNodeWithText(expectedLabel).assertExists()
    }

    @Test
    fun simplePopup_pieChartDatapoint_displaysCorrectly() {
        val dataPoint = ColoredChartDataPoint(
            1f,
            "Test Label",
            Color.Red
        )
        composeTestRule.setContent {
            SimplePopup(
                modifier = Modifier,
                dataPoint = dataPoint
            )
        }

        composeTestRule.onNodeWithText("Test Label").assertExists()
        composeTestRule.onNodeWithText("1.0").assertExists()
    }

    @Test
    fun simplePopup_emptyTitle_labelDisplaysCorrectly() {
        composeTestRule.setContent {
            SimplePopup(
                modifier = Modifier,
                title = "",
                label = ""
            )
        }

        composeTestRule.onAllNodesWithText("").assertCountEquals(2)
    }
}