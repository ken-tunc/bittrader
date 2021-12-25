package org.kentunc.bittrader.web.infrastructure.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.OrderApiClient
import org.kentunc.bittrader.web.domain.repository.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val orderApiClient: OrderApiClient) : OrderRepository {

    override suspend fun find(productCode: ProductCode): OrderList {
        return orderApiClient.get(productCode)
            .map { it.toOrder() }
            .toList()
            .let { OrderList.of(it) }
    }
}
