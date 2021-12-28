package org.kentunc.bittrader.web.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.order.BalanceApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.OrderApiClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OrderApiClientConfigurationProperties::class)
class OrderApiClientConfiguration(private val properties: OrderApiClientConfigurationProperties) {

    @Bean
    fun orderApiWebClient() = WebClient.create(properties.url.toString())

    @Bean
    fun orderApiClient() = OrderApiClient(orderApiWebClient())

    @Bean
    fun balanceApiClient() = BalanceApiClient(orderApiWebClient())
}
