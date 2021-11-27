package org.kentunc.bittrader.candle.api.presentation.router

import org.kentunc.bittrader.candle.api.presentation.handler.CandleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CandleRouter(private val candleHandler: CandleHandler) {

    @Bean
    fun candleRoutes() = coRouter {
        "/candles".nest {
            PUT("/feed", candleHandler::feed)
        }
    }
}
