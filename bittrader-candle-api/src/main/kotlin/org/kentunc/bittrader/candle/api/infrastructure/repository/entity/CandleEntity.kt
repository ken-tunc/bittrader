package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("candle")
data class CandleEntity(
    // NOTE: composite primary key is not supported yet...
    // c.f.) https://github.com/spring-projects/spring-data-r2dbc/issues/158
    @field:Id
    val productCode: ProductCode,
    val duration: Duration,
    val dateTime: LocalDateTime,
    val open: BigDecimal,
    val close: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val volume: BigDecimal
) {

    val primaryKey: CandlePrimaryKey
        get() = CandlePrimaryKey(productCode, duration, dateTime)

    companion object {
        fun of(candle: Candle) = CandleEntity(
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
