package org.kentunc.bittrader.common.domain.model.candle

import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime

class Candle private constructor(
    val id: CandleId,
    val open: Price,
    val close: Price,
    val high: Price,
    val low: Price,
    val volume: Volume
) {

    init {
        require(low <= high) { "`high` is less than `low`, but high=$high, low=$low" }
    }

    companion object {
        fun of(id: CandleId, open: Price, close: Price, high: Price, low: Price, volume: Volume) =
            Candle(id, open, close, high, low, volume)

        fun of(ticker: Ticker, duration: Duration): Candle {
            val id = CandleId(ticker.id.productCode, duration, TruncatedDateTime.of(ticker.id.dateTime, duration))
            val price = ticker.midPrice
            return Candle(id, price, price, price, price, ticker.volume)
        }
    }

    fun extendWith(ticker: Ticker): Candle {
        require(this.id.productCode == ticker.id.productCode) {
            "product code mismatch: this=${this.id.productCode}, ticker=${ticker.id.productCode}"
        }
        require(this.id.dateTime == TruncatedDateTime.of(ticker.id.dateTime, this.id.duration)) {
            "datetime mismatch: this=${this.id.dateTime}, ticker=${ticker.id.dateTime}, " +
                    "duration=${this.id.duration}"
        }

        val price = ticker.midPrice
        return Candle(
            id = this.id,
            open = this.open,
            close = price,
            high = maxOf(this.high, price),
            low = minOf(this.low, price),
            volume = this.volume + ticker.volume
        )
    }
}
