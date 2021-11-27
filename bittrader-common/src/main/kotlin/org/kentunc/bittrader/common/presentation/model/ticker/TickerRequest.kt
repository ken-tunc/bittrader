package org.kentunc.bittrader.common.presentation.model.ticker

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.ticker.TickerId
import org.kentunc.bittrader.common.domain.model.time.DateTime
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.validation.constraints.DecimalMin

data class TickerRequest(
    val productCode: ProductCode,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) val dateTime: LocalDateTime,
    val bestBid: BigDecimal,
    val bestAsk: BigDecimal,
    @field:DecimalMin("0") val volume: BigDecimal
) {

    companion object {
        fun of(ticker: Ticker) = TickerRequest(
            productCode = ticker.id.productCode,
            dateTime = ticker.id.dateTime.toLocalDateTime(),
            bestBid = ticker.bestBid.toBigDecimal(),
            bestAsk = ticker.bestAsk.toBigDecimal(),
            volume = ticker.volume.toBigDecimal()
        )
    }

    fun toModel() = Ticker.of(
        id = TickerId(productCode, DateTime.of(dateTime)),
        bestBid = Price.of(bestBid),
        bestAsk = Price.of(bestAsk),
        volume = Volume.of(volume)
    )
}
