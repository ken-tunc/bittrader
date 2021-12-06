package org.kentunc.bittrader.candle.feeder.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.candle.feeder.domain.exception.UnexpectedSubscribeException
import org.kentunc.bittrader.candle.feeder.domain.repository.TickerRepository
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.BitflyerRealtimeTickerClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class TickerRepositoryImpl(private val bitflyerRealtimeTickerClient: BitflyerRealtimeTickerClient) : TickerRepository {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun subscribe(productCode: ProductCode): Flow<Ticker> {
        try {
            return bitflyerRealtimeTickerClient.subscribe(productCode)
                .map { it.toTicker() }
        } catch (ex: Exception) {
            log.info("subscription exception type: {}", ex::class.java.canonicalName)
            throw UnexpectedSubscribeException(ex.message, ex)
        }
    }
}
