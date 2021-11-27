package org.kentunc.bittrader.common.test.model

import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime
import java.time.LocalDateTime

object TestCandle {

    fun create(
        productCode: ProductCode = ProductCode.BTC_JPY,
        duration: Duration = Duration.DAYS,
        dateTime: LocalDateTime = LocalDateTime.of(2021, 1, 1, 12, 0, 0),
        open: Double = 100.0,
        close: Double = 120.0,
        high: Double = 130.0,
        low: Double = 90.0,
        volume: Double = 100.0
    ): Candle {
        val id = CandleId(productCode, duration, TruncatedDateTime.of(dateTime, duration))
        return Candle.of(
            id,
            open = Price.of(open),
            close = Price.of(close),
            high = Price.of(high),
            low = Price.of(low),
            volume = Volume.of(volume)
        )
    }
}
