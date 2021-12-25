package org.kentunc.bittrader.common.presentation.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestOrder

internal class OrderRequestTest {

    @Test
    fun testOf() {
        val order = TestOrder.createOrder()
        val actual = OrderRequest.of(order)
        assertAll(
            { assertEquals(order.detail.productCode, actual.detail.productCode) },
            { assertEquals(order.detail.orderType, actual.detail.orderType) },
            { assertEquals(order.detail.orderSide, actual.detail.orderSide) },
            { assertEquals(order.detail.size.toBigDecimal(), actual.detail.size) },
            { assertEquals(order.detail.price?.toBigDecimal(), actual.detail.price) },
            { assertEquals(order.minutesToExpire.toInt(), actual.minutesToExpire) },
            { assertEquals(order.timeInForce, actual.timeInForce) },
        )
    }

    @Test
    fun testToOrder() {
        val order = TestOrder.createOrder()
        val orderRequest = OrderRequest(
            detail = OrderDetailDto(
                productCode = order.detail.productCode,
                orderType = order.detail.orderType,
                orderSide = order.detail.orderSide,
                size = order.detail.size.toBigDecimal(),
                price = order.detail.price?.toBigDecimal()
            ),
            minutesToExpire = order.minutesToExpire.toInt(),
            timeInForce = order.timeInForce
        )
        val actual = orderRequest.toOrder()
        assertAll(
            { assertEquals(order.detail.productCode, actual.detail.productCode) },
            { assertEquals(order.detail.orderType, actual.detail.orderType) },
            { assertEquals(order.detail.orderSide, actual.detail.orderSide) },
            { assertEquals(order.detail.size, actual.detail.size) },
            { assertEquals(order.detail.price, actual.detail.price) },
            { assertEquals(order.minutesToExpire, actual.minutesToExpire) },
            { assertEquals(order.timeInForce, actual.timeInForce) },
        )
    }
}
