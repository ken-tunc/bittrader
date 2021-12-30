package org.kentunc.bittrader.order.api.domain.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker

interface TickerRepository {

    suspend fun get(productCode: ProductCode): Ticker
}
