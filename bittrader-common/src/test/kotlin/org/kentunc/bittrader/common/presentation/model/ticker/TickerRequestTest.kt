package org.kentunc.bittrader.common.presentation.model.ticker

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.test.model.TestTicker
import java.math.BigDecimal
import java.time.LocalDateTime

internal class TickerRequestTest {

    @Test
    fun testOf() {
        val ticker = TestTicker.create()
        val actual = TickerRequest.of(ticker)
        assertAll(
            { assertEquals(ticker.id.productCode, actual.productCode) },
            { assertEquals(ticker.id.dateTime.toLocalDateTime(), actual.dateTime) },
            { assertEquals(ticker.bestBid.toBigDecimal(), actual.bestBid) },
            { assertEquals(ticker.bestAsk.toBigDecimal(), actual.bestAsk) },
            { assertEquals(ticker.volume.toBigDecimal(), actual.volume) },
        )
    }

    @Test
    fun testToTicker() {
        val request = TickerRequest(
            productCode = ProductCode.BTC_JPY,
            dateTime = LocalDateTime.now(),
            bestBid = BigDecimal.valueOf(200.0),
            bestAsk = BigDecimal.valueOf(100.0),
            volume = BigDecimal.valueOf(150.0)
        )
        val actual = request.toTicker()
        assertAll(
            { assertEquals(request.productCode, actual.id.productCode) },
            { assertEquals(request.dateTime, actual.id.dateTime.toLocalDateTime()) },
            { assertEquals(request.bestBid, actual.bestBid.toBigDecimal()) },
            { assertEquals(request.bestAsk, actual.bestAsk.toBigDecimal()) },
            { assertEquals(request.volume, actual.volume.toBigDecimal()) },
        )
    }
}
