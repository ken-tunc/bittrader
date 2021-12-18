package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.presentation.model.market.BalanceResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.exchangeToFlow

class BalanceApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/balances"
    }

    fun get(): Flow<BalanceResponse> =
        webClient.get()
            .uri(PATH_BASE)
            .exchangeToFlow { it.bodyToFlow() }
}
