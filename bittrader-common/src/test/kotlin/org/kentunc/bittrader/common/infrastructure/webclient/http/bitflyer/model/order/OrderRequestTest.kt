package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestOrder

internal class OrderRequestTest {

    @Test
    fun testOf() {
        val order = TestOrder.createBuyAll()
        val actual = OrderRequest.of(order)
        assertAll(
            { assertEquals(order.detail.productCode, actual.productCode) },
            { assertEquals(order.detail.orderType, actual.orderType) },
            { assertEquals(order.detail.orderSide, actual.side) },
            { assertEquals(order.detail.price?.toBigDecimal(), actual.price) },
            { assertEquals(order.detail.size.toBigDecimal(), actual.size) },
            { assertEquals(order.minutesToExpire.toInt(), actual.minutesToExpire) },
            { assertEquals(order.timeInForce, actual.timeInForce) },
        )
    }
}
