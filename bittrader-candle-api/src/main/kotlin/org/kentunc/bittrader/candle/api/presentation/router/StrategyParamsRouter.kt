package org.kentunc.bittrader.candle.api.presentation.router

import org.kentunc.bittrader.candle.api.presentation.handler.StrategyParamsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class StrategyParamsRouter(private val strategyParamsHandler: StrategyParamsHandler) {

    @Bean
    fun strategyParamsRoutes() = coRouter {
        "/strategies/params".nest {
            GET("/summary") { strategyParamsHandler.getParamsSummary(it) }
            PUT("") { strategyParamsHandler.updateParams(it) }
        }
    }
}
