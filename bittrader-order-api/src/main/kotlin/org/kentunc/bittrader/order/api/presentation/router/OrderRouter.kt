package org.kentunc.bittrader.order.api.presentation.router

import org.kentunc.bittrader.order.api.presentation.handler.OrderHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OrderRouter(private val orderHandler: OrderHandler) {

    @Bean
    fun orderRoutes() = coRouter {
        "/orders".nest {
            GET("/{productCode}") { orderHandler.get(it) }
            POST("") { orderHandler.send(it) }
        }
    }
}
