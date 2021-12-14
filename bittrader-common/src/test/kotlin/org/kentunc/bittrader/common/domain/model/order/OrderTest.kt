package org.kentunc.bittrader.common.domain.model.order

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal
import java.util.stream.Stream

internal class OrderTest {

    @ParameterizedTest
    @ArgumentsSource(ValidRequestProvider::class)
    fun instantiate(orderType: OrderType, price: Price?) {
        assertDoesNotThrow {
            Order(
                productCode = ProductCode.BTC_JPY,
                orderType = orderType,
                orderSide = OrderSide.SELL,
                price = price,
                size = Size.of(BigDecimal.TEN),
                minutesToExpire = MinutesToExpire.of(10),
                timeInForce = TimeInForce.FOK
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidRequestProvider::class)
    fun instantiate_invalid(orderType: OrderType, price: Price?) {
        assertThrows<IllegalArgumentException> {
            Order(
                productCode = ProductCode.ETH_JPY,
                orderType = orderType,
                orderSide = OrderSide.BUY,
                price = price,
                size = Size.of(BigDecimal.ONE),
                minutesToExpire = MinutesToExpire.of(30),
                timeInForce = TimeInForce.GTC
            )
        }
    }

    @Test
    fun testOfSellAll_valid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(productCode.left, Size.of(1000.0), Size.Companion.of(1000.0))
        val minutesToExpire = MinutesToExpire.of(30)
        val timeInForce = TimeInForce.FOK

        // exercise:
        val actual = Order.ofSellAll(productCode, balance, minutesToExpire, timeInForce)

        // verify:
        assertAll(
            { assertEquals(productCode, actual.productCode) },
            { assertEquals(OrderType.MARKET, actual.orderType) },
            { assertEquals(OrderSide.SELL, actual.orderSide) },
            { assertNull(actual.price) },
            { assertEquals(balance.available, actual.size) },
            { assertEquals(minutesToExpire, actual.minutesToExpire) },
            { assertEquals(timeInForce, actual.timeInForce) },
        )
    }

    @Test
    fun testOfSellAll_invalid() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = Balance.of(CurrencyCode.ETH, Size.of(1000.0), Size.Companion.of(1000.0))

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
            { assertEquals(productCode, actual.productCode) },
            { assertEquals(OrderType.MARKET, actual.orderType) },
            { assertEquals(OrderSide.BUY, actual.orderSide) },
            { assertNull(actual.price) },
            { assertEquals(balance.available - fee, actual.size) },
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

    companion object {
        private class ValidRequestProvider : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    // OrderType, Price
                    arguments(OrderType.LIMIT, Price.of(BigDecimal.TEN)),
                    arguments(OrderType.MARKET, null)
                )
            }
        }

        private class InvalidRequestProvider : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
                return Stream.of(
                    // OrderType, Price
                    arguments(OrderType.LIMIT, null),
                    arguments(OrderType.MARKET, Price.of(BigDecimal.TEN))
                )
            }
        }
    }
}
