package org.kentunc.bittrader.common.presentation.model.market

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestBalance

internal class BalanceResponseTest {

    @Test
    fun testOf() {
        val balance = TestBalance.create()
        val actual = BalanceResponse.of(balance)
        assertAll(
            { assertEquals(balance.currencyCode, actual.currencyCode) },
            { assertEquals(balance.amount.toBigDecimal(), actual.amount) },
            { assertEquals(balance.available.toBigDecimal(), actual.available) },
        )
    }

    @Test
    fun testToBalance() {
        val balance = TestBalance.create()
        val balanceResponse = BalanceResponse(
            currencyCode = balance.currencyCode,
            amount = balance.amount.toBigDecimal(),
            available = balance.available.toBigDecimal()
        )
        val actual = balanceResponse.toBalance()
        assertAll(
            { assertEquals(balance.currencyCode, actual.currencyCode) },
            { assertEquals(balance.amount, actual.amount) },
            { assertEquals(balance.available, actual.available) },
        )
    }
}
