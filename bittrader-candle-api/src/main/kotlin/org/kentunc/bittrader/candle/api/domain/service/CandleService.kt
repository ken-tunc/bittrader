package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.repository.CandleRepository
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class CandleService(private val candleRepository: CandleRepository) {

    @Transactional(propagation = Propagation.MANDATORY)
    suspend fun feed(ticker: Ticker, duration: Duration): Void? {
        val newCandle = Candle.of(ticker, duration)
        return when (val matched = candleRepository.find(newCandle.id)) {
            null -> candleRepository.save(newCandle)
            else -> candleRepository.update(matched.extendWith(ticker))
        }
    }
}
