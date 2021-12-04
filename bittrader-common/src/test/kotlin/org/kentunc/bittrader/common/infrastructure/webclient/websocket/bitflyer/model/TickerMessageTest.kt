package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import java.math.BigDecimal

internal class TickerMessageTest {

    @Test
    fun testToTicker() {
        // setup:
        val tickerMessage = TickerMessage(
            productCode = ProductCode.BTC_JPY,
            timestamp = "2019-04-11T05:14:12.3739915Z",
            bestBid = BigDecimal.valueOf(100.0),
            bestAsk = BigDecimal.valueOf(200.0),
            volume = BigDecimal.valueOf(150.0)
        )

        // exercise:
        val actual = tickerMessage.toTicker()

        // verify:
        assertAll(
            { assertEquals(tickerMessage.productCode, actual.id.productCode) },
            { assertEquals(tickerMessage.bestBid, actual.bestBid.toBigDecimal()) },
            { assertEquals(tickerMessage.bestAsk, actual.bestAsk.toBigDecimal()) },
            { assertEquals(tickerMessage.volume, actual.volume.toBigDecimal()) },
        )
    }
}
