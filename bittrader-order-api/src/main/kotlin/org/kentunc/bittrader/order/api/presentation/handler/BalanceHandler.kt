package org.kentunc.bittrader.order.api.presentation.handler

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.common.presentation.model.market.BalanceResponse
import org.kentunc.bittrader.order.api.application.BalanceInteractor
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class BalanceHandler(private val balanceInteractor: BalanceInteractor) {

    suspend fun get(request: ServerRequest): ServerResponse {
        val balances = balanceInteractor.getBalances()
            .map { BalanceResponse.of(it) }
            .toList()
        return ServerResponse.ok()
            .bodyValueAndAwait(balances)
    }
}
