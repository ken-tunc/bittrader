package org.kentunc.bittrader.order.api.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.Order

interface OrderRepository {

    fun find(productCode: ProductCode): Flow<Order>
    suspend fun send(orderSignal: OrderSignal): Void?
}
