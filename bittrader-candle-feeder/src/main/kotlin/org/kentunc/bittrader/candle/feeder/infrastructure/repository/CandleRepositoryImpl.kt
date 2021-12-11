package org.kentunc.bittrader.candle.feeder.infrastructure.repository

import org.kentunc.bittrader.candle.feeder.domain.repository.CandleRepository
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.infrastructure.webclient.rsocket.candle.CandleApiClient
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class CandleRepositoryImpl(private val candleApiClient: CandleApiClient) : CandleRepository {

    override suspend fun feed(ticker: Ticker): Void? {
        return candleApiClient.feed(TickerRequest.of(ticker))
    }
}
