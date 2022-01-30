package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.order.scheduler.domain.repository.CandleRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class CandleRepositoryImpl(private val candleApiClient: CandleApiClient) : CandleRepository {

    override suspend fun getCandleList(productCode: ProductCode, duration: Duration, maxNum: Int) {
        val request = CandleSearchRequest(productCode, duration, maxNum)
        return candleApiClient.search(request)
            .map { it.toCandle() }
            .toList()
            .let { CandleList.of(it) }
    }
}
