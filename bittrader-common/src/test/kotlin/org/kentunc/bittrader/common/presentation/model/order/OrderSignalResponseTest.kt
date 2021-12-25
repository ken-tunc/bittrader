package org.kentunc.bittrader.common.presentation.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestOrder

internal class OrderSignalResponseTest {

    @Test
    fun testOf() {
        val orderSignal = TestOrder.createOrderSignal()
        val actual = OrderSignalResponse.of(orderSignal)
        assertAll(
            { assertEquals(orderSignal.detail.productCode, actual.detail.productCode) },
            { assertEquals(orderSignal.detail.orderType, actual.detail.orderType) },
            { assertEquals(orderSignal.detail.orderSide, actual.detail.orderSide) },
            { assertEquals(orderSignal.detail.size.toBigDecimal(), actual.detail.size) },
            { assertEquals(orderSignal.detail.price?.toBigDecimal(), actual.detail.price) },
            { assertEquals(orderSignal.averagePrice.toBigDecimal(), actual.averagePrice) },
            { assertEquals(orderSignal.state, actual.state) },
            { assertEquals(orderSignal.orderDate.toLocalDateTime(), actual.orderDate) },
        )
    }

    @Test
    fun testToOrderSignal() {
        val orderSignal = TestOrder.createOrderSignal()
        val orderSignalResponse = OrderSignalResponse(
            detail = OrderDetailDto(
                productCode = orderSignal.detail.productCode,
                orderType = orderSignal.detail.orderType,
                orderSide = orderSignal.detail.orderSide,
                size = orderSignal.detail.size.toBigDecimal(),
                price = orderSignal.detail.price?.toBigDecimal()
            ),
            averagePrice = orderSignal.averagePrice.toBigDecimal(),
            state = orderSignal.state,
            orderDate = orderSignal.orderDate.toLocalDateTime()
        )
        val actual = orderSignalResponse.toOrderSignal()
        assertAll(
            { assertEquals(orderSignal.detail.productCode, actual.detail.productCode) },
            { assertEquals(orderSignal.detail.orderType, actual.detail.orderType) },
            { assertEquals(orderSignal.detail.orderSide, actual.detail.orderSide) },
            { assertEquals(orderSignal.detail.size, actual.detail.size) },
            { assertEquals(orderSignal.detail.price, actual.detail.price) },
            { assertEquals(orderSignal.averagePrice, actual.averagePrice) },
            { assertEquals(orderSignal.state, actual.state) },
            { assertEquals(orderSignal.orderDate, actual.orderDate) },
        )
    }
}
