package org.kentunc.bittrader.common.presentation.model.candle

import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime
import java.math.BigDecimal
import java.time.LocalDateTime

data class CandleResponse(
    val productCode: ProductCode,
    val duration: Duration,
    val dateTime: LocalDateTime,
    val open: BigDecimal,
    val close: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val volume: BigDecimal
) {

    companion object {
        fun of(candle: Candle) = CandleResponse(
            productCode = candle.id.productCode,
            duration = candle.id.duration,
            dateTime = candle.id.dateTime.toLocalDateTime(),
            open = candle.open.toBigDecimal(),
            close = candle.close.toBigDecimal(),
            high = candle.high.toBigDecimal(),
            low = candle.low.toBigDecimal(),
            volume = candle.volume.toBigDecimal()
        )
    }

    fun toCandle() = Candle.of(
        id = CandleId(productCode, duration, TruncatedDateTime.of(dateTime, duration)),
        open = Price.of(open),
        close = Price.of(close),
        high = Price.of(high),
        low = Price.of(low),
        volume = Volume.of(volume)
    )
}
