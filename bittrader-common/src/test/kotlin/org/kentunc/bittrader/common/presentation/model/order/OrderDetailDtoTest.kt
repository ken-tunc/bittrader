package org.kentunc.bittrader.common.presentation.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.test.model.TestOrder
import java.util.stream.Stream

internal class OrderDetailDtoTest {

    @ParameterizedTest
    @ArgumentsSource(OrderPriceProvider::class)
    fun testOf(orderType: OrderType, price: Price?) {
        val detail = TestOrder.createDetail(orderType = orderType, price = price)
        val actual = OrderDetailDto.of(detail)
        assertAll(
            { assertEquals(detail.productCode, actual.productCode) },
            { assertEquals(detail.orderType, actual.orderType) },
            { assertEquals(detail.orderSide, actual.orderSide) },
            { assertEquals(detail.size.toBigDecimal(), actual.size) },
            { assertEquals(detail.price?.toBigDecimal(), actual.price) },
        )
    }

    @ParameterizedTest
    @ArgumentsSource(OrderPriceProvider::class)
    fun testToOrderDetail(orderType: OrderType, price: Price?) {
        val detail = TestOrder.createDetail(orderType = orderType, price = price)
        val dto = OrderDetailDto(
            productCode = detail.productCode,
            orderType = detail.orderType,
            orderSide = detail.orderSide,
            size = detail.size.toBigDecimal(),
            price = detail.price?.toBigDecimal()
        )
        val actual = dto.toOrderDetail()
        assertAll(
            { assertEquals(detail.productCode, actual.productCode) },
            { assertEquals(detail.orderType, actual.orderType) },
            { assertEquals(detail.orderSide, actual.orderSide) },
            { assertEquals(detail.size, actual.size) },
            { assertEquals(detail.price, actual.price) }
        )
    }

    private class OrderPriceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.arguments(OrderType.LIMIT, Price.of(100.0)),
                Arguments.arguments(OrderType.MARKET, null)
            )
        }
    }
}
