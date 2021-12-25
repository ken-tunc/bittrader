package org.kentunc.bittrader.order.api.presentation.handler

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.common.presentation.model.order.OrderResponse
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.kentunc.bittrader.order.api.application.OrderInteractor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class OrderHandler(private val requestValidator: RequestValidator, private val orderInteractor: OrderInteractor) {

    suspend fun get(request: ServerRequest): ServerResponse {
        val productCode = request.pathVariable("productCode")
            .let { ProductCode.valueOf(it) }
        val orderSignals = orderInteractor.getOrderListByProductCode(productCode)
            .toList()
            .map { OrderResponse.of(it) }
        return ServerResponse.ok()
            .bodyValueAndAwait(orderSignals)
    }

    suspend fun send(request: ServerRequest): ServerResponse {
        val order = request.awaitBody<OrderSignalRequest>()
        requestValidator.validate(order)
        orderInteractor.sendOrder(order.toOrderSignal())
        return ServerResponse.noContent()
            .buildAndAwait()
    }
}
