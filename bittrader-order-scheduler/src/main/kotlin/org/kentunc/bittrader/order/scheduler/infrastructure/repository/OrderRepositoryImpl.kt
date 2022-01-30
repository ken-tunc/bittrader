package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.OrderApiClient
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
class OrderRepositoryImpl(private val orderApiClient: OrderApiClient) : OrderRepository {

    override suspend fun getOrderList(productCode: ProductCode): OrderList {
        return orderApiClient.get(productCode)
            .map { it.toOrder() }
            .toList()
            .let { OrderList.of(it) }
    }

    override suspend fun sendOrder(productCode: ProductCode, orderSide: OrderSide): Void? {
        return orderApiClient.send(OrderSignalRequest(productCode, orderSide))
    }
}
