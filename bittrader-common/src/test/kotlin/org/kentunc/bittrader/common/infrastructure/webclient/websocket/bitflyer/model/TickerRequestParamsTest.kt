package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode

internal class TickerRequestParamsTest {

    @Test
    fun testChannel() {
        val params = TickerRequestParams(ProductCode.BTC_JPY)
        assertEquals("lightning_ticker_BTC_JPY", params.channel)
    }
}
