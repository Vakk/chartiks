package com.valeriik.chart.compose.panel

sealed interface XAxisScope {
    companion object {
        private const val DEFAULT_7D = 1000 * 60 * 60 * 24 * 7
    }

    class DateScope(
        content: (DateScope.() -> Unit)? = null
    ) : XAxisScope {
        var fromTimestamp: Long = System.currentTimeMillis() - DEFAULT_7D
        var toTimestamp: Long = System.currentTimeMillis() + DEFAULT_7D

        init {
            content?.let { apply(content) }
        }
    }

    class IndexScope(
        content: (IndexScope.() -> Unit)? = null
    ) : XAxisScope {
        var itemsPerScreen = 10

        init {
            content?.let { apply(content) }
        }
    }
}