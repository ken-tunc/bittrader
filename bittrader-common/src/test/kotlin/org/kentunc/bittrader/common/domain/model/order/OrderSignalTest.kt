package org.kentunc.bittrader.common.domain.model.order

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.stream.Stream

internal class OrderSignalTest {

    @ParameterizedTest
    @ArgumentsSource(ValidRequestProvider::class)
    fun instantiate(orderType: OrderType, price: Price?) {
        assertDoesNotThrow {
            OrderSignal.of(
                productCode = ProductCode.BTC_JPY,
                orderType = orderType,
                orderSide = OrderSide.SELL,
                price = price,
                size = Size.of(BigDecimal.TEN),
                averagePrice = Price.of(1000.0),
                state = OrderState.COMPLETED,
                orderDate = DateTime.of(LocalDateTime.now())
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidRequestProvider::class)
    fun instantiate_invalid(orderType: OrderType, price: Price?) {
        assertThrows<IllegalArgumentException> {
            OrderSignal.of(
                productCode = ProductCode.ETH_JPY,
                orderType = orderType,
                orderSide = OrderSide.BUY,
                price = price,
                size = Size.of(BigDecimal.ONE),
                averagePrice = Price.of(1000.0),
                state = OrderState.COMPLETED,
                orderDate = DateTime.of(LocalDateTime.now())
            )
        }
    }

    companion object {
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
}
