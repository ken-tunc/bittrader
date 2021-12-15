package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import java.math.BigDecimal

internal class BalanceResponseTest {

    @Test
    fun testToBalance() {
        val response = BalanceResponse(
            currencyCode = CurrencyCode.BTC,
            amount = BigDecimal.valueOf(200.0),
            available = BigDecimal.valueOf(100.0)
        )
        val actual = response.toBalance()
        assertAll(
            { assertEquals(response.currencyCode, actual.currencyCode) },
            { assertEquals(response.amount, actual.amount.toBigDecimal()) },
            { assertEquals(response.available, actual.available.toBigDecimal()) },
        )
    }
}
