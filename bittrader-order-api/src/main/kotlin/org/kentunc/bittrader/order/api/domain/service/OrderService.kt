package org.kentunc.bittrader.order.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    suspend fun getOrderSignalList(productCode: ProductCode): OrderList {
        return orderRepository.find(productCode)
            .toList()
            .let { OrderList.of(it) }
    }

    suspend fun send(orderSignal: OrderSignal): Void? {
        return orderRepository.send(orderSignal)
    }
}
