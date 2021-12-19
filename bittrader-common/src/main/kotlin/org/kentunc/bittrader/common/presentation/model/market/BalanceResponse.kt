package org.kentunc.bittrader.common.presentation.model.market

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal

data class BalanceResponse(val currencyCode: CurrencyCode, val amount: BigDecimal, val available: BigDecimal) {

    companion object {
        fun of(balance: Balance) = BalanceResponse(
            currencyCode = balance.currencyCode,
            amount = balance.amount.toBigDecimal(),
            available = balance.available.toBigDecimal()
        )
    }

    fun toBalance() = Balance.of(currencyCode = currencyCode, amount = Size.of(amount), available = Size.of(available))
}
