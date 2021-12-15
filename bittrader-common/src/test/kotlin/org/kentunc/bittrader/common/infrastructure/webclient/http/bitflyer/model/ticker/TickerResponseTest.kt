package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.ticker

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import java.math.BigDecimal
import java.time.LocalDateTime

internal class TickerResponseTest {

    @Test
    fun testToTicker() {
        val response = TickerResponse(
            productCode = ProductCode.BTC_JPY,
            timestamp = LocalDateTime.now(),
            bestBid = BigDecimal.valueOf(200.0),
            bestAsk = BigDecimal.valueOf(100.0),
            volume = BigDecimal.valueOf(150.0)
        )
        val actual = response.toTicker()
        assertAll(
            { assertEquals(response.productCode, actual.id.productCode) },
            { assertEquals(response.timestamp, actual.id.dateTime.toLocalDateTime()) },
            { assertEquals(response.bestBid, actual.bestBid.toBigDecimal()) },
            { assertEquals(response.bestAsk, actual.bestAsk.toBigDecimal()) },
            { assertEquals(response.volume, actual.volume.toBigDecimal()) },
        )
    }
}
