package org.kentunc.bittrader.common.domain.model.candle

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.common.test.model.TestCandleList

internal class CandleListExtensionsKtTest {

    @Test
    fun testToBarSeries() {
        val candleList = TestCandleList.create()
        val actual = candleList.toBarSeries()
        assertAll(
            { assertEquals(candleList.size, actual.barCount) },
            { assertEquals(candleList.toList()[0].open.toBigDecimal().toDouble(), actual.firstBar.openPrice.doubleValue()) },
            { assertEquals(candleList.toList()[0].close.toBigDecimal().toDouble(), actual.firstBar.closePrice.doubleValue()) },
            { assertEquals(candleList.toList()[0].high.toBigDecimal().toDouble(), actual.firstBar.highPrice.doubleValue()) },
            { assertEquals(candleList.toList()[0].low.toBigDecimal().toDouble(), actual.firstBar.lowPrice.doubleValue()) },
            { assertEquals(candleList.toList()[0].volume.toBigDecimal().toDouble(), actual.firstBar.volume.doubleValue()) },
        )
    }
}
