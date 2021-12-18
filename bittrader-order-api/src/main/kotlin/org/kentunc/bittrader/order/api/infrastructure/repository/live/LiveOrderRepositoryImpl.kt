package org.kentunc.bittrader.order.api.infrastructure.repository.live

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order.OrderRequest
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class LiveOrderRepositoryImpl(private val bitflyerClient: BitflyerHttpPrivateApiClient) : OrderRepository {

    override fun find(productCode: ProductCode): Flow<OrderSignal> {
        return bitflyerClient.getOrders(productCode)
            .map { it.toOrderSignal() }
    }

    override suspend fun send(order: Order): Void? {
        return bitflyerClient.sendOrder(OrderRequest.of(order))
    }
}
