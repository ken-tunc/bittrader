package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.reactor.awaitSingle
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.market.CommissionRateResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class CommissionRateApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/commission-rates"
    }

    suspend fun get(productCode: ProductCode): CommissionRateResponse =
        webClient.get()
            .uri {
                it.path("$PATH_BASE/{productCode}")
                    .build(productCode)
            }
            .exchangeToMono { it.bodyToMono<CommissionRateResponse>() }
            .awaitSingle()
}
