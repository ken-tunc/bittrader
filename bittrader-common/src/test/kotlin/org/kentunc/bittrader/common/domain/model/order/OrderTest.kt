package org.kentunc.bittrader.common.domain.model.order

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Size

internal class OrderTest {

    @Test
    fun testOfSellAll_valid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(productCode.left, Size.of(1000.0), Size.of(1000.0))
        val minutesToExpire = MinutesToExpire.of(30)
        val timeInForce = TimeInForce.FOK

        // exercise:
        val actual = Order.ofSellAll(productCode, balance, minutesToExpire, timeInForce)

        // verify:
        assertAll(
            { assertEquals(productCode, actual.detail.productCode) },
            { assertEquals(OrderType.MARKET, actual.detail.orderType) },
            { assertEquals(OrderSide.SELL, actual.detail.orderSide) },
            { assertNull(actual.detail.price) },
            { assertEquals(balance.available, actual.detail.size) },
            { assertEquals(minutesToExpire, actual.minutesToExpire) },
            { assertEquals(timeInForce, actual.timeInForce) },
        )
    }

    @Test
    fun testOfSellAll_invalid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(CurrencyCode.ETH, Size.of(1000.0), Size.of(1000.0))

        // exercise & verify:
        assertThrows<IllegalArgumentException> {
            Order.ofSellAll(productCode, balance)
        }
    }

    @Test
    fun testOfBuyAll() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(productCode.right, Size.of(1000.0), Size.of(1000.0))
        val commissionRate = mockk<CommissionRate>()
        val minutesToExpire = MinutesToExpire.of(30)
        val timeInForce = TimeInForce.FOK
        val fee = Size.of(900.0)
        every { commissionRate.fee(any()) } returns fee

        // exercise:
        val actual = Order.ofBuyAll(productCode, balance, commissionRate, minutesToExpire, timeInForce)

        // verify:
        assertAll(
            { assertEquals(productCode, actual.detail.productCode) },
            { assertEquals(OrderType.MARKET, actual.detail.orderType) },
            { assertEquals(OrderSide.BUY, actual.detail.orderSide) },
            { assertNull(actual.detail.price) },
            { assertEquals(balance.available - fee, actual.detail.size) },
            { assertEquals(minutesToExpire, actual.minutesToExpire) },
            { assertEquals(timeInForce, actual.timeInForce) },
        )
    }

    @Test
    fun testOfBuyAll_invalid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(CurrencyCode.ETH, Size.of(1000.0), Size.Companion.of(1000.0))
        val commissionRate = CommissionRate.of(0.0015)

        // exercise & verify:
        assertThrows<IllegalArgumentException> {
            Order.ofBuyAll(productCode, balance, commissionRate)
        }
    }
}
