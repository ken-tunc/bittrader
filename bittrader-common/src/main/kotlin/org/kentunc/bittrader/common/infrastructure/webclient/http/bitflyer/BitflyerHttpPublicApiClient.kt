package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import kotlinx.coroutines.reactive.awaitFirst
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.ticker.TickerResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class BitflyerHttpPublicApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_TICKER = "/ticker"
    }

    suspend fun getTicker(productCode: ProductCode): TickerResponse {
        return webClient.get()
            .uri {
                it.path(PATH_TICKER)
                    .queryParam("product_code", productCode)
                    .build()
            }
            .exchangeToMono { it.bodyToMono<TickerResponse>() }
            .awaitFirst()
    }
}
