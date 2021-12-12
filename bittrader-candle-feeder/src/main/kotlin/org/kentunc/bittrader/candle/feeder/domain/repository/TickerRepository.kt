package org.kentunc.bittrader.candle.feeder.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker

interface TickerRepository {

    fun subscribe(productCodes: Collection<ProductCode>): Flow<Ticker>
}
