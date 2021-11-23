package org.kentunc.bittrader.common.domain.model.ticker

import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume

class Ticker private constructor(val id: TickerId, val bestBid: Price, val bestAsk: Price, val volume: Volume) {

    companion object {
        fun of(id: TickerId, bestBid: Price, bestAsk: Price, volume: Volume) = Ticker(id, bestBid, bestAsk, volume)
    }

    val midPrice: Price
        get() = (bestBid + bestAsk) / Price.of(2)
}
