package org.kentunc.bittrader.candle.api.presentation.router

import org.kentunc.bittrader.candle.api.presentation.handler.StrategyHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class StrategyRouter(private val strategyHandler: StrategyHandler) {

    @Bean
    fun strategyRoutes() = coRouter {
        "/strategies".nest {
            GET("/positions") { strategyHandler.getPosition(it) }
            POST("/optimize") { strategyHandler.optimize(it) }
        }
    }
}
