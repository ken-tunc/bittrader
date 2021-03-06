package org.kentunc.bittrader.order.scheduler.domain.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide

interface OrderRepository {

    suspend fun getOrderList(productCode: ProductCode): OrderList
    suspend fun sendOrder(productCode: ProductCode, orderSide: OrderSide): Void?
}
