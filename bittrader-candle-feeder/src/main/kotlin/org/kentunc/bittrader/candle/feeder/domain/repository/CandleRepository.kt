package org.kentunc.bittrader.candle.feeder.domain.repository

import org.kentunc.bittrader.common.domain.model.ticker.Ticker

interface CandleRepository {

    suspend fun feed(ticker: Ticker): Void?
}
