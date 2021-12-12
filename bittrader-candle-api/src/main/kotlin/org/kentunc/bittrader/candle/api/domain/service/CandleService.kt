package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.repository.CandleRepository
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CandleService(private val candleRepository: CandleRepository) {

    @Transactional(readOnly = true)
    suspend fun findLatest(query: CandleQuery) : CandleList {
        return candleRepository.find(query)
            .toList()
            .let { CandleList.of(it) }
    }

    @Transactional
    suspend fun feed(ticker: Ticker, duration: Duration): Void? {
        val newCandle = Candle.of(ticker, duration)
        return when (val matched = candleRepository.findOne(newCandle.id)) {
            null -> candleRepository.save(newCandle)
            else -> candleRepository.update(matched.extendWith(ticker))
        }
    }
}
