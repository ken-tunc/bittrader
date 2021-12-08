package org.kentunc.bittrader.candle.feeder.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.candle.feeder.domain.repository.TickerRepository
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.BitflyerRealtimeTickerClient
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class TickerRepositoryImpl(private val bitflyerRealtimeTickerClient: BitflyerRealtimeTickerClient) : TickerRepository {

    override fun subscribe(productCodes: Collection<ProductCode>): Flow<Ticker> {
        return bitflyerRealtimeTickerClient.subscribe(productCodes)
            .map { it.toTicker() }
    }
}
