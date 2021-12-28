package org.kentunc.bittrader.order.scheduler.domain.service

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import org.kentunc.bittrader.order.scheduler.domain.repository.BalanceRepository
import org.kentunc.bittrader.order.scheduler.domain.repository.CommissionRateRepository
import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val balanceRepository: BalanceRepository,
    private val commissionRateRepository: CommissionRateRepository,
    private val orderRepository: OrderRepository
) {

    suspend fun sendBuyAllOrderIfPossible(
        productCode: ProductCode,
        minutesToExpire: MinutesToExpire,
        timeInForce: TimeInForce
    ): Void? {
        val orderList = orderRepository.getOrderList(productCode)
        if (!orderList.canBuy()) {
            return null
        }

        val balance = balanceRepository.get(productCode.right) ?: return null
        val commissionRate = commissionRateRepository.get(productCode)
        val orderSignal = OrderSignal.ofBuyAll(productCode, balance, commissionRate, minutesToExpire, timeInForce)
        return orderRepository.sendOrder(orderSignal)
    }

    suspend fun sendSellAllOrderIfPossible(
        productCode: ProductCode,
        minutesToExpire: MinutesToExpire,
        timeInForce: TimeInForce
    ): Void? {
        val orderList = orderRepository.getOrderList(productCode)
        if (!orderList.canSell()) {
            return null
        }

        val balance = balanceRepository.get(productCode.left) ?: return null
        val orderSignal = OrderSignal.ofSellAll(productCode, balance, minutesToExpire, timeInForce)
        return orderRepository.sendOrder(orderSignal)
    }
}
