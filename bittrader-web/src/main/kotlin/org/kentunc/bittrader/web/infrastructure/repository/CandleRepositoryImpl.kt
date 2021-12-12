package org.kentunc.bittrader.web.infrastructure.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.web.domain.repository.CandleRepository
import org.springframework.stereotype.Repository

@Repository
class CandleRepositoryImpl(private val candleApiClient: CandleApiClient) : CandleRepository {

    override suspend fun search(query: CandleQuery): CandleList {
        return candleApiClient.search(CandleSearchRequest.of(query))
            .map { it.toCandle() }
            .toList()
            .let { CandleList.of(it) }
    }
}
