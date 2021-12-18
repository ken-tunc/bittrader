package org.kentunc.bittrader.order.api.application

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSignalList
import org.kentunc.bittrader.order.api.domain.service.OrderService
import org.springframework.stereotype.Service

@Service
class OrderInteractor(private val orderService: OrderService) {

    suspend fun getOrderSignalListByProductId(productCode: ProductCode): OrderSignalList =
        orderService.getOrderSignalList(productCode)

    suspend fun sendOrder(order: Order) {
        orderService.send(order)
    }
}
