package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.ticker.TickerId
import org.kentunc.bittrader.common.domain.model.time.DateTimeFactory
import java.math.BigDecimal

data class TickerMessage(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val timestamp: String,
    @field:JsonProperty("best_bid")
    val bestBid: BigDecimal,
    @field:JsonProperty("best_ask")
    val bestAsk: BigDecimal,
    val volume: BigDecimal
) {

    fun toTicker() = Ticker.of(
        id = TickerId(productCode, DateTimeFactory.getByTimestamp(timestamp)),
        bestBid = Price.of(bestBid),
        bestAsk = Price.of(bestAsk),
        volume = Volume.of(volume)
    )
}
