package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal

data class BalanceResponse(
    @field:JsonProperty("currency_code")
    val currencyCode: String,
    val amount: BigDecimal,
    val available: BigDecimal
) {

    fun toBalance() = Balance.of(
        currencyCode = CurrencyCode.valueOf(currencyCode),
        amount = Size.of(amount),
        available = Size.of(available)
    )

    fun isValid(): Boolean = runCatching { CurrencyCode.valueOf(currencyCode) }.isSuccess
}
