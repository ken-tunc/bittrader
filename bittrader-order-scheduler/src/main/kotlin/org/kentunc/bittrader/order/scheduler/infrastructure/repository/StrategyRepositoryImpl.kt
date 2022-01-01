package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.StrategyApiClient
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.order.scheduler.domain.repository.StrategyRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class StrategyRepositoryImpl(private val strategyApiClient: StrategyApiClient) : StrategyRepository {

    override suspend fun getTradePosition(productCode: ProductCode, duration: Duration): TradingPosition {
        return strategyApiClient.getPosition(TradePositionRequest(productCode, duration))
            .position
    }

    override suspend fun optimize(productCode: ProductCode, duration: Duration): Void? {
        return strategyApiClient.optimize(OptimizeRequest(productCode, duration))
    }
}
