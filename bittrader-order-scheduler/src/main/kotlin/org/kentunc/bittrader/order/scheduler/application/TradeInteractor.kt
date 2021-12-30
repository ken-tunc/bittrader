package org.kentunc.bittrader.order.scheduler.application

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.order.scheduler.domain.service.OrderService
import org.kentunc.bittrader.order.scheduler.domain.service.StrategyService
import org.springframework.stereotype.Service

@Service
class TradeInteractor(private val strategyService: StrategyService, private val orderService: OrderService) {

    suspend fun trade(productCode: ProductCode, duration: Duration) {
        when (strategyService.getTradePosition(productCode, duration)) {
            TradePosition.SHOULD_BUY -> {
                if (orderService.getOrderList(productCode).canBuy()) {
                    orderService.sendOrder(productCode, OrderSide.BUY)
                }
            }
            TradePosition.SHOULD_SELL -> {
                if (orderService.getOrderList(productCode).canSell()) {
                    orderService.sendOrder(productCode, OrderSide.SELL)
                }
            }
            TradePosition.NEUTRAL -> Unit
        }
    }

    suspend fun optimizeStrategies(productCode: ProductCode, duration: Duration) {
        strategyService.optimize(productCode, duration)
    }
}
