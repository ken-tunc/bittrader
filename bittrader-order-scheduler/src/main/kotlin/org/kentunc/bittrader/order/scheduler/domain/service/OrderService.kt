package org.kentunc.bittrader.order.scheduler.domain.service

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    suspend fun getOrderList(productCode: ProductCode): OrderList {
        return orderRepository.getOrderList(productCode)
    }

    suspend fun sendOrder(productCode: ProductCode, orderSide: OrderSide): Void? {
        return orderRepository.sendOrder(productCode, orderSide)
    }
}
