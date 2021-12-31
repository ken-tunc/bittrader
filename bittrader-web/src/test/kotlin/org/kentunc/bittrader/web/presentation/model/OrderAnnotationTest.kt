package org.kentunc.bittrader.web.presentation.model

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.test.model.TestCandle
import org.kentunc.bittrader.common.test.model.TestOrder
import java.time.LocalDateTime

internal class OrderAnnotationTest {

    @ParameterizedTest
    @CsvSource("true,false", "false,true", "true,true")
    fun testOf_empty(candleListIsEmpty: Boolean, orderListIsEmpty: Boolean) {
        // setup:
        val candleList = mockk<CandleList>()
        every { candleList.isEmpty } returns candleListIsEmpty
        val orderList = mockk<OrderList>()
        every { orderList.isEmpty } returns orderListIsEmpty

        // exercise:
        val actual = OrderAnnotation.of(candleList, orderList)

        // verify:
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testOf() {
        // setup:
        val baseDateTime = LocalDateTime.now()
        val candles = listOf(
            TestCandle.create(dateTime = baseDateTime),
            TestCandle.create(dateTime = baseDateTime.plusDays(1)),
        )
        val candleList = mockk<CandleList>()
        every { candleList.isEmpty } returns false
        every { candleList.toList() } returns candles

        val orders = listOf(
            TestOrder.createOrder(orderDate = baseDateTime.minusDays(-1)),
            TestOrder.createOrder(orderDate = baseDateTime),
            TestOrder.createOrder(orderDate = baseDateTime.plusDays(1)),
        )
        val orderList = mockk<OrderList>()
        every { orderList.isEmpty } returns false
        every { orderList.toList() } returns orders

        // exercise:
        val actual = OrderAnnotation.of(candleList, orderList)

        // verify:
        assertEquals(1, actual.size)
    }
}
