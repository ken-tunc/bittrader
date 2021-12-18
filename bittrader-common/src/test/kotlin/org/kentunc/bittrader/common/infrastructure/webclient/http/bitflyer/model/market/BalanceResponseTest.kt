package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import java.math.BigDecimal

internal class BalanceResponseTest {

    @Test
    fun testToBalance() {
        val currencyCode = CurrencyCode.BTC
        val response = BalanceResponse(
            currencyCode = currencyCode.toString(),
            amount = BigDecimal.valueOf(200.0),
            available = BigDecimal.valueOf(100.0)
        )
        val actual = response.toBalance()
        assertAll(
            { assertEquals(currencyCode, actual.currencyCode) },
            { assertEquals(response.amount, actual.amount.toBigDecimal()) },
            { assertEquals(response.available, actual.available.toBigDecimal()) },
        )
    }

    @Test
    fun testIsValid_valid() {
        val response = BalanceResponse(
            currencyCode = CurrencyCode.BTC.toString(),
            amount = BigDecimal.valueOf(200.0),
            available = BigDecimal.valueOf(100.0)
        )
        assertTrue(response.isValid())
    }

    @Test
    fun testIsValid_invalid() {
        val response = BalanceResponse(
            currencyCode = "invalid",
            amount = BigDecimal.valueOf(200.0),
            available = BigDecimal.valueOf(100.0)
        )
        assertFalse(response.isValid())
    }
}
