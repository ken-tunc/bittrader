package org.kentunc.bittrader.web.presentation.model

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import java.math.BigDecimal

data class BalanceRow(val currencyCode: CurrencyCode, val amount: BigDecimal, val available: BigDecimal) {

    companion object {
        fun of(balance: Balance) = BalanceRow(
            currencyCode = balance.currencyCode,
            amount = balance.amount.toBigDecimal(),
            available = balance.available.toBigDecimal()
        )
    }
}
