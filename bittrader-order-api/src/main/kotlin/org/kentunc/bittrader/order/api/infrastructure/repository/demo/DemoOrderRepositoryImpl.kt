package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository

class DemoOrderRepositoryImpl(private val demoBroker: DemoBroker) : OrderRepository {

    override fun find(productCode: ProductCode): Flow<OrderSignal> {
        return demoBroker.getOrderSignals(productCode)
            .asFlow()
    }

    override suspend fun send(order: Order): Void? {
        return demoBroker.sendOrder(order)
            .let { null }
    }
}
