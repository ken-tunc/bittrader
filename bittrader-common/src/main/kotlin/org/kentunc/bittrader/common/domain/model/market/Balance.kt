package org.kentunc.bittrader.common.domain.model.market

import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size

class Balance private constructor(val currencyCode: CurrencyCode, val amount: Size, val available: Size) {

    companion object {
        fun of(currencyCode: CurrencyCode, amount: Size, available: Size) = Balance(currencyCode, amount, available)
    }

    fun buySize(price: Price): Size = available / price
}
