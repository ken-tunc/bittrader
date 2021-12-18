package org.kentunc.bittrader.order.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSignalList
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    suspend fun getOrderSignalList(productCode: ProductCode): OrderSignalList {
        return orderRepository.find(productCode)
            .toList()
            .let { OrderSignalList.of(it) }
    }

    suspend fun send(order: Order): Void? {
        return orderRepository.send(order)
    }
}
