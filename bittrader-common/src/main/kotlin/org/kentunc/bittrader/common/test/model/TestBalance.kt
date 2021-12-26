package org.kentunc.bittrader.common.test.model

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal

object TestBalance {

    fun create(
        currencyCode: CurrencyCode = CurrencyCode.BTC,
        amount: BigDecimal = BigDecimal.valueOf(2000.0),
        available: BigDecimal = BigDecimal.valueOf(1900.0)
    ): Balance {
        return Balance.of(
            currencyCode = currencyCode,
            amount = Size.of(amount),
            available = Size.of(available)
        )
    }
}
