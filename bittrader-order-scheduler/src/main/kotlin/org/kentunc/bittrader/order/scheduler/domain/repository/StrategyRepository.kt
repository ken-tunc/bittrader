package org.kentunc.bittrader.order.scheduler.domain.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.kentunc.bittrader.common.domain.model.time.Duration

interface StrategyRepository {

    suspend fun getTradePosition(productCode: ProductCode, duration: Duration): TradingPosition
    suspend fun optimize(productCode: ProductCode, duration: Duration): Void?
}
