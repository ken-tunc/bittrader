package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.quote.Price
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.stream.Stream

internal class OrderResponseTest {

    @ParameterizedTest
    @ArgumentsSource(OrderPriceProvider::class)
    fun testToOrderSignal(orderType: OrderType, priceResponse: BigDecimal, expectedPrice: Price?) {
        val response = OrderResponse(
            productCode = ProductCode.BTC_JPY,
            side = OrderSide.BUY,
            orderType = orderType,
            price = priceResponse,
            size = BigDecimal.valueOf(150.0),
            averagePrice = BigDecimal.valueOf(100.0),
            state = OrderState.COMPLETED,
            orderDate = LocalDateTime.now()
        )
        val actual = response.toOrderSignal()
        assertAll(
            { assertEquals(response.productCode, actual.detail.productCode) },
            { assertEquals(response.side, actual.detail.orderSide) },
            { assertEquals(response.orderType, actual.detail.orderType) },
            { assertEquals(expectedPrice, actual.detail.price) },
            { assertEquals(response.size, actual.detail.size.toBigDecimal()) },
            { assertEquals(response.averagePrice, actual.averagePrice.toBigDecimal()) },
            { assertEquals(response.state, actual.state) },
            { assertEquals(response.orderDate, actual.orderDate.toLocalDateTime()) },
        )
    }

    private class OrderPriceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // OrderType, Price
                Arguments.arguments(OrderType.LIMIT, BigDecimal.TEN, Price.of(BigDecimal.TEN)),
                Arguments.arguments(OrderType.MARKET, BigDecimal.ZERO, null)
            )
        }
    }
}
