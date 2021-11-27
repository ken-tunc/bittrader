package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CandleInteractor(private val candleService: CandleService) {

    @Transactional
    suspend fun feedCandlesByTicker(ticker: Ticker) {
        Duration.values().forEach {
            candleService.feed(ticker, it)
        }
    }
}
