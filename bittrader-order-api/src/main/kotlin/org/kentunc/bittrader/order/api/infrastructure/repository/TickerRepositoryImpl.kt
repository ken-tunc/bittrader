package org.kentunc.bittrader.order.api.infrastructure.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.order.api.domain.repository.TickerRepository
import org.springframework.stereotype.Repository

@Repository
class TickerRepositoryImpl(private val bitflyerClient: BitflyerHttpPublicApiClient) : TickerRepository {

    override suspend fun get(productCode: ProductCode): Ticker {
        return bitflyerClient.getTicker(productCode)
            .toTicker()
    }
}
