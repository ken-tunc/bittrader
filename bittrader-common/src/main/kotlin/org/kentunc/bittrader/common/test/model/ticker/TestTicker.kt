package org.kentunc.bittrader.common.test.model.ticker

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.ticker.TickerId
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.time.LocalDateTime

object TestTicker {

    fun create(
        productCode: ProductCode = ProductCode.BTC_JPY,
        dateTime: LocalDateTime = LocalDateTime.now(),
        bestBid: Double = 100.0,
        bestAsk: Double = 200.0,
        volume: Double = 150.0
    ): Ticker {
        val id = TickerId(productCode, DateTime.of(dateTime))
        return Ticker.of(id, bestBid = Price.of(bestBid), bestAsk = Price.of(bestAsk), volume = Volume.of(volume))
    }
}
