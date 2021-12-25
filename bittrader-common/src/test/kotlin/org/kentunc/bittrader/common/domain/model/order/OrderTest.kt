package org.kentunc.bittrader.common.domain.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.time.DateTime
import org.kentunc.bittrader.common.test.model.TestOrder
import java.time.LocalDateTime

internal class OrderTest {

    @Test
    fun testOf() {
        // setup:
        val detail = TestOrder.createDetail()
        val averagePrice = Price.of(1000.0)
        val state = OrderState.COMPLETED
        val orderDate = DateTime.of(LocalDateTime.now())

        // exercise:
        val actual = Order.of(
            detail = OrderDetail.of(
                productCode = detail.productCode,
                orderType = detail.orderType,
                orderSide = detail.orderSide,
                price = detail.price,
                size = detail.size
            ),
            averagePrice = averagePrice,
            state = state,
            orderDate = orderDate
        )

        // verify:
        assertAll(
            { assertEquals(detail.productCode, actual.detail.productCode) },
            { assertEquals(detail.orderType, actual.detail.orderType) },
            { assertEquals(detail.orderSide, actual.detail.orderSide) },
            { assertEquals(detail.price, actual.detail.price) },
            { assertEquals(detail.size, actual.detail.size) },
            { assertEquals(averagePrice, actual.averagePrice) },
            { assertEquals(state, actual.state) },
            { assertEquals(orderDate, actual.orderDate) },
        )
    }
}
