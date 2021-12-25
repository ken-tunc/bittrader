package org.kentunc.bittrader.order.api.presentation.handler

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.order.OrderRequest
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalResponse
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.kentunc.bittrader.order.api.application.OrderInteractor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class OrderHandler(private val requestValidator: RequestValidator, private val orderInteractor: OrderInteractor) {

    suspend fun get(request: ServerRequest): ServerResponse {
        val productCode = request.pathVariable("productCode")
            .let { ProductCode.valueOf(it) }
        val orderSignals = orderInteractor.getOrderSignalListByProductCode(productCode)
            .toList()
            .map { OrderSignalResponse.of(it) }
        return ServerResponse.ok()
            .bodyValueAndAwait(orderSignals)
    }

    suspend fun send(request: ServerRequest): ServerResponse {
        val order = request.awaitBody<OrderRequest>()
        requestValidator.validate(order)
        orderInteractor.sendOrder(order.toOrder())
        return ServerResponse.noContent()
            .buildAndAwait()
    }
}
