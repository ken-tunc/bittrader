package org.kentunc.bittrader.order.api.application

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.order.api.domain.service.OrderService
import org.springframework.stereotype.Service

@Service
class OrderInteractor(private val orderService: OrderService) {

    suspend fun getOrderListByProductCode(productCode: ProductCode): OrderList =
        orderService.getOrderSignalList(productCode)

    suspend fun sendOrder(orderSignal: OrderSignal) {
        orderService.send(orderSignal)
    }
}
