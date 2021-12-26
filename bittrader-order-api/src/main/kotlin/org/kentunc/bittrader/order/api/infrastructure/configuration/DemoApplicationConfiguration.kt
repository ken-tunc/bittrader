package org.kentunc.bittrader.order.api.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.order.api.infrastructure.configuration.properties.DemoApplicationConfigurationProperties
import org.kentunc.bittrader.order.api.infrastructure.repository.demo.DemoBalanceRepositoryImpl
import org.kentunc.bittrader.order.api.infrastructure.repository.demo.DemoBroker
import org.kentunc.bittrader.order.api.infrastructure.repository.demo.DemoCommissionRateRepositoryImpl
import org.kentunc.bittrader.order.api.infrastructure.repository.demo.DemoDatabase
import org.kentunc.bittrader.order.api.infrastructure.repository.demo.DemoOrderRepositoryImpl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Configuration
@Profile("demo")
@EnableConfigurationProperties(DemoApplicationConfigurationProperties::class)
class DemoApplicationConfiguration(private val properties: DemoApplicationConfigurationProperties) {

    @Bean
    fun demoDatabase(): DemoDatabase =
        properties.balances.associate { it.currencyCode to BigDecimal.valueOf(it.amount) }
            .let { DemoDatabase(it) }

    @Bean
    fun demoBroker(bitflyerHttpPublicApiClient: BitflyerHttpPublicApiClient) =
        DemoBroker(demoDatabase(), bitflyerHttpPublicApiClient, commissionRateRepository())

    @Bean
    @Primary
    fun balanceRepository(demoBroker: DemoBroker) = DemoBalanceRepositoryImpl(demoBroker)

    @Bean
    @Primary
    fun commissionRateRepository() = DemoCommissionRateRepositoryImpl(properties.commissionRate)

    @Bean
    @Primary
    fun orderRepository(demoBroker: DemoBroker) = DemoOrderRepositoryImpl(demoBroker)
}
