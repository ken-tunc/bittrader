package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CandleInteractor(private val candleService: CandleService) {

    @Transactional(readOnly = true)
    suspend fun findLatestCandles(query: CandleQuery) : CandleList {
        return candleService.findLatest(query)
    }

    @Transactional
    suspend fun feedCandlesByTicker(ticker: Ticker) {
        Duration.values().forEach {
            candleService.feed(ticker, it)
        }
    }
}
