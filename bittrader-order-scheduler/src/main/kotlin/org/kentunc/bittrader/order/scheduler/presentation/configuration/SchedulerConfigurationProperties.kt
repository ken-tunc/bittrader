package org.kentunc.bittrader.order.scheduler.presentation.configuration

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.scheduling")
data class SchedulerConfigurationProperties(val productCode: ProductCode, val duration: Duration)
