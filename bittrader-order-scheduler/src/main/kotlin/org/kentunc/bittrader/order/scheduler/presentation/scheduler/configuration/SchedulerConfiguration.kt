package org.kentunc.bittrader.order.scheduler.presentation.scheduler.configuration

import org.kentunc.bittrader.order.scheduler.application.TradeInteractor
import org.kentunc.bittrader.order.scheduler.presentation.scheduler.TradeScheduler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SchedulerConfigurationProperties::class)
class SchedulerConfiguration(private val properties: SchedulerConfigurationProperties) {

    @Bean
    fun tradeScheduler(tradeInteractor: TradeInteractor) =
        TradeScheduler(properties.productCode, properties.duration, tradeInteractor)
}
