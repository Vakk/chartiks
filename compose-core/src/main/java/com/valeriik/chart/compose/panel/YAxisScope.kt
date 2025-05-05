package com.valeriik.chart.compose.panel

class YAxisScope {
    var verticalSpacingThreshold: Float = 0.1f

    constructor(content: (YAxisScope.() -> Unit)? = null) {
        content?.let { apply(content) }
    }
}