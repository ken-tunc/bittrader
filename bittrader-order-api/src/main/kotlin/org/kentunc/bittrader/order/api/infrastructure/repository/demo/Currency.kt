package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import java.math.BigDecimal

data class Currency(
    val currencyCode: CurrencyCode,
    val size: BigDecimal
)
