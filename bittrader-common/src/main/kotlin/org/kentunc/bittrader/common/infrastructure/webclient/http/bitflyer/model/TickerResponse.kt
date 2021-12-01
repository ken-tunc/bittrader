package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.ticker.TickerId
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.LocalDateTime

data class TickerResponse(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val timestamp: LocalDateTime,
    @field:JsonProperty("best_bid")
    val bestBid: BigDecimal,
    @field:JsonProperty("best_ask")
    val bestAsk: BigDecimal,
    val volume: BigDecimal
) {

    fun toModel() = Ticker.of(
        id = TickerId(productCode = productCode, dateTime = DateTime.of(timestamp)),
        bestBid = Price.of(bestBid),
        bestAsk = Price.of(bestAsk),
        volume = Volume.of(volume)
    )
}
