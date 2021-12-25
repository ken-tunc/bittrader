package org.kentunc.bittrader.order.api.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSignal

interface OrderRepository {

    fun find(productCode: ProductCode): Flow<OrderSignal>
    suspend fun send(order: Order): Void?
}
