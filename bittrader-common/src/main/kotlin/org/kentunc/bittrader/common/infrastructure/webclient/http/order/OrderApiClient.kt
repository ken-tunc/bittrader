package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.order.OrderRequest
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.exchangeToFlow

class OrderApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/orders"
    }

    fun get(productCode: ProductCode): Flow<OrderSignalResponse> =
        webClient.get()
            .uri {
                it.path("$PATH_BASE/{productCode}")
                    .build(productCode)
            }
            .exchangeToFlow { it.bodyToFlow() }

    suspend fun send(orderRequest: OrderRequest): Void? =
        webClient.post()
            .uri(PATH_BASE)
            .bodyValue(orderRequest)
            .exchangeToMono { it.releaseBody() }
            .awaitFirstOrNull()
}
