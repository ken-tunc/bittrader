package org.kentunc.bittrader.common.domain.model.order

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.test.model.TestTicker

internal class OrderSignalTest {

    @Test
    fun testOfSellAll_valid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(productCode.left, Size.of(1000.0), Size.of(1000.0))
        val minutesToExpire = MinutesToExpire.of(30)
        val timeInForce = TimeInForce.FOK

        // exercise:
        val actual = OrderSignal.ofSellAll(productCode, balance, minutesToExpire, timeInForce)

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
            OrderSignal.ofSellAll(productCode, balance)
        }
    }

    @Test
    fun testOfBuyAll() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(productCode.right, Size.of(1000000.0), Size.of(1000000.0))
        val commissionRate = mockk<CommissionRate>()
        val ticker = TestTicker.create(productCode = productCode)
        val minutesToExpire = MinutesToExpire.of(30)
        val timeInForce = TimeInForce.FOK
        val fee = Size.of(900.0)
        every { commissionRate.fee(any()) } returns fee

        // exercise:
        val actual = OrderSignal.ofBuyAll(productCode, balance, commissionRate, ticker, minutesToExpire, timeInForce)

        // verify:
        assertAll(
            { assertEquals(productCode, actual.detail.productCode) },
            { assertEquals(OrderType.LIMIT, actual.detail.orderType) },
            { assertEquals(OrderSide.BUY, actual.detail.orderSide) },
            { assertEquals(ticker.bestAsk, actual.detail.price) },
            { assertEquals(minutesToExpire, actual.minutesToExpire) },
            { assertEquals(timeInForce, actual.timeInForce) },
        )
    }

    @ParameterizedTest
    @CsvSource("BTC_JPY,ETH,BTC_JPY", "BTC_JPY,JPY,ETH_JPY")
    fun testOfBuyAll_invalid(productCode: ProductCode, currencyCode: CurrencyCode, tickerProductCode: ProductCode) {
        // setup:
        val balance = Balance.of(currencyCode, Size.of(1000.0), Size.Companion.of(1000.0))
        val commissionRate = CommissionRate.of(0.0015)
        val ticker = TestTicker.create(productCode = tickerProductCode)

        // exercise & verify:
        assertThrows<IllegalArgumentException> {
            OrderSignal.ofBuyAll(productCode, balance, commissionRate, ticker)
        }
    }
}
