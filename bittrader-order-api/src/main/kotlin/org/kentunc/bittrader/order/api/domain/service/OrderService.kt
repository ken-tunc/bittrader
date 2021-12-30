package org.kentunc.bittrader.order.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.order.api.domain.factory.OrderFactory
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderFactory: OrderFactory, private val orderRepository: OrderRepository) {

    suspend fun getOrderSignalList(productCode: ProductCode): OrderList {
        return orderRepository.find(productCode)
            .toList()
            .let { OrderList.of(it) }
    }

    suspend fun sendBuyAllOrder(productCode: ProductCode): Void? {
        val orderSignal = orderFactory.createBuyAllOrder(productCode)
        return orderRepository.send(orderSignal)
    }

    suspend fun sendSellAllOrder(productCode: ProductCode): Void? {
        val orderSignal = orderFactory.createSellAllOrder(productCode)
        return orderRepository.send(orderSignal)
    }
}
