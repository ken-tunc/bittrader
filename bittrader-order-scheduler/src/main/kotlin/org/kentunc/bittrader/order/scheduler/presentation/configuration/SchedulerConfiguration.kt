package org.kentunc.bittrader.order.scheduler.presentation.configuration

import org.kentunc.bittrader.order.scheduler.application.TradeInteractor
import org.kentunc.bittrader.order.scheduler.presentation.scheduler.TradeScheduler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@Profile("scheduling")
@EnableConfigurationProperties(SchedulerConfigurationProperties::class)
class SchedulerConfiguration(private val properties: SchedulerConfigurationProperties) {

    @Bean
    fun tradeScheduler(tradeInteractor: TradeInteractor) =
        TradeScheduler(properties.productCode, properties.duration, tradeInteractor)
}
