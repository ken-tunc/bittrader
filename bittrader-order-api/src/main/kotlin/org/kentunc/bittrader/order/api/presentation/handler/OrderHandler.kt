package org.kentunc.bittrader.order.api.presentation.handler

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.presentation.model.order.OrderResponse
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.kentunc.bittrader.order.api.application.OrderInteractor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

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
        val orderRequest = request.awaitBody<OrderSignalRequest>()
        requestValidator.validate(orderRequest)
        when (orderRequest.orderSide) {
            OrderSide.BUY -> orderInteractor.sendBuyAllOrder(orderRequest.productCode)
            OrderSide.SELL -> orderInteractor.sendSellAllOrder(orderRequest.productCode)
        }
        return ServerResponse.noContent()
            .buildAndAwait()
    }
}
