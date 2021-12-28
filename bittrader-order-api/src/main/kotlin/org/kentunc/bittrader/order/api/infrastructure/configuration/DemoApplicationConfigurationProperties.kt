package org.kentunc.bittrader.order.api.infrastructure.configuration

import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.math.BigDecimal

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.demo")
class DemoApplicationConfigurationProperties(
    val balances: List<InitialBalance>,
    val commissionRate: BigDecimal
) {
    data class InitialBalance(
        val currencyCode: CurrencyCode,
        val amount: Double
    )
}
