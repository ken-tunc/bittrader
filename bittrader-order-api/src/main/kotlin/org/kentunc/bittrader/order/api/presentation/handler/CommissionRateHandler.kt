package org.kentunc.bittrader.order.api.presentation.handler

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.market.CommissionRateResponse
import org.kentunc.bittrader.order.api.application.CommissionRateInteractor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class CommissionRateHandler(private val commissionRateInteractor: CommissionRateInteractor) {

    suspend fun get(request: ServerRequest): ServerResponse {
        val productCode = request.pathVariable("productCode")
            .let { ProductCode.valueOf(it) }
        val commissionRate = commissionRateInteractor.getByProductCode(productCode)
        return ServerResponse.ok()
            .bodyValueAndAwait(CommissionRateResponse.of(commissionRate))
    }
}
