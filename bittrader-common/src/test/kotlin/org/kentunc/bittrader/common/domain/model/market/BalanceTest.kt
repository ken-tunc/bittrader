package org.kentunc.bittrader.common.domain.model.market

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.quote.Size

internal class BalanceTest {

    @Test
    fun testOf() {
        // setup:
        val currencyCode = CurrencyCode.BTC
        val amount = Size.of(1000.0)
        val available = Size.of(900.0)

        // exercise:
        val actual = Balance.of(currencyCode, amount, available)

        // verify:
        assertAll(
            { assertEquals(currencyCode, actual.currencyCode) },
            { assertEquals(amount, actual.amount) },
            { assertEquals(available, actual.available) },
        )
    }
}
