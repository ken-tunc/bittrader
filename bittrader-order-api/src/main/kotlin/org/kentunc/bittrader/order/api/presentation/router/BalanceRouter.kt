package org.kentunc.bittrader.order.api.presentation.router

import org.kentunc.bittrader.order.api.presentation.handler.BalanceHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class BalanceRouter(private val balanceHandler: BalanceHandler) {

    @Bean
    fun balanceRoutes() = coRouter {
        "/balances".nest {
            GET("") { balanceHandler.get(it) }
        }
    }
}
