package org.kentunc.bittrader.order.api.presentation.router

import org.kentunc.bittrader.order.api.presentation.handler.CommissionRateHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CommissionRateRouter(private val commissionRateHandler: CommissionRateHandler) {

    @Bean
    fun commissionRateRoutes() = coRouter {
        "/commission-rates".nest {
            GET("/{productCode}") { commissionRateHandler.get(it) }
        }
    }
}
