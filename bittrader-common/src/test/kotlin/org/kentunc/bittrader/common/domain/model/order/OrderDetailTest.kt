package org.kentunc.bittrader.common.domain.model.order

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal
import java.util.stream.Stream

internal class OrderDetailTest {

    @ParameterizedTest
    @ArgumentsSource(ValidRequestProvider::class)
    fun instantiate(orderType: OrderType, price: Price?) {
        assertDoesNotThrow {
            OrderDetail.of(
                productCode = ProductCode.BTC_JPY,
                orderType = orderType,
                orderSide = OrderSide.SELL,
                price = price,
                size = Size.of(BigDecimal.TEN)
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidRequestProvider::class)
    fun instantiate_invalid(orderType: OrderType, price: Price?) {
        assertThrows<IllegalArgumentException> {
            OrderDetail.of(
                productCode = ProductCode.ETH_JPY,
                orderType = orderType,
                orderSide = OrderSide.BUY,
                price = price,
                size = Size.of(BigDecimal.ONE)
            )
        }
    }

    @Test
    fun instantiate_invalidSide() {
        assertThrows<IllegalArgumentException> {
            OrderDetail.of(
                productCode = ProductCode.ETH_JPY,
                orderType = OrderType.MARKET,
                orderSide = OrderSide.NEUTRAL,
                price = null,
                size = Size.of(BigDecimal.ONE)
            )
        }
    }

    private class ValidRequestProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // OrderType, Price
                Arguments.arguments(OrderType.LIMIT, Price.of(BigDecimal.TEN)),
                Arguments.arguments(OrderType.MARKET, null)
            )
        }
    }

    private class InvalidRequestProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // OrderType, Price
                Arguments.arguments(OrderType.LIMIT, null),
                Arguments.arguments(OrderType.MARKET, Price.of(BigDecimal.TEN))
            )
        }
    }
}
