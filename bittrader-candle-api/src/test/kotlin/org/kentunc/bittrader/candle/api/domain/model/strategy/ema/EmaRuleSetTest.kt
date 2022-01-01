package org.kentunc.bittrader.candle.api.domain.model.strategy.ema

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.common.test.model.TestCandleList

internal class EmaRuleSetTest {

    @Test
    fun testOf() {
        val barSeries = TestCandleList.create().toBarSeries()
        val params = EmaParams(8, 15)
        assertDoesNotThrow { EmaRuleSet.of(barSeries, params) }
    }
}
