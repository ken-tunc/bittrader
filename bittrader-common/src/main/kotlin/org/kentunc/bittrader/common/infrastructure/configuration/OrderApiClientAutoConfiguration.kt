package org.kentunc.bittrader.common.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.ClientHttpConnectorFactory
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.BalanceApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.CommissionRateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.OrderApiClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OrderApiClientConfigurationProperties::class)
@ConditionalOnProperty(prefix = "bittrader.internal.order-api", name = ["url"])
class OrderApiClientAutoConfiguration(
    private val properties: OrderApiClientConfigurationProperties,
    private val connectorFactory: ClientHttpConnectorFactory
) {
    @Bean
    fun orderApiWebClient(): WebClient {
        val connector = connectorFactory.create(properties.connectTimeout, properties.readTimeout)
        return WebClient.builder()
            .baseUrl(properties.url.toString())
            .clientConnector(connector)
            .build()
    }

    @Bean
    fun balanceApiClient() = BalanceApiClient(orderApiWebClient())

    @Bean
    fun commissionRateApiClient() = CommissionRateApiClient(orderApiWebClient())

    @Bean
    fun orderApiClient() = OrderApiClient(orderApiWebClient())
}
