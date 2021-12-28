package org.kentunc.bittrader.order.scheduler.application

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.order.scheduler.domain.service.OrderService
import org.kentunc.bittrader.order.scheduler.domain.service.StrategyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TradeInteractor(
    private val strategyService: StrategyService,
    private val orderService: OrderService,
    @Value("\${bittrader.trade.order.minutes-to-expire}")
    minutesToExpire: Int,
    @Value("\${bittrader.trade.order.time-in-force}")
    private val timeInForce: TimeInForce
) {
    private val minutesToExpire = MinutesToExpire.of(minutesToExpire)

    suspend fun trade(productCode: ProductCode, duration: Duration) {
        when (strategyService.getTradePosition(productCode, duration)) {
            TradePosition.SHOULD_BUY -> orderService.sendBuyAllOrderIfPossible(
                productCode,
                minutesToExpire,
                timeInForce
            )
            TradePosition.SHOULD_SELL -> orderService.sendSellAllOrderIfPossible(
                productCode,
                minutesToExpire,
                timeInForce
            )
            TradePosition.NEUTRAL -> Unit
        }
    }

    suspend fun optimizeStrategies(productCode: ProductCode, duration: Duration) {
        strategyService.optimize(productCode, duration)
    }
}
