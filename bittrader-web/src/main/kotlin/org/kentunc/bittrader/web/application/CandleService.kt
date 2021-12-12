package org.kentunc.bittrader.web.application

import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.web.domain.repository.CandleRepository
import org.springframework.stereotype.Service

@Service
class CandleService(private val candleRepository: CandleRepository) {

    suspend fun search(query: CandleQuery): CandleList = candleRepository.search(query)
}
