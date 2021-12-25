package org.kentunc.bittrader.common.domain.model.order

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.test.model.TestOrder
import java.time.LocalDateTime
import java.util.stream.Stream

internal class OrderListTest {

    @Test
    fun testSorted() {
        // setup:
        val baseDateTime = LocalDateTime.now()
        val oldestOrder = TestOrder.createOrder(orderDate = baseDateTime)
        val intermediateOrder = TestOrder.createOrder(orderDate = baseDateTime.plusMinutes(1))
        val latestOrder = TestOrder.createOrder(orderDate = baseDateTime.plusHours(1))
        val orders = listOf(intermediateOrder, latestOrder, oldestOrder)

        // exercise
        val orderList = OrderList.of(orders)

        // verify
        assertAll(
            { assertFalse(orderList.isEmpty) },
            { assertEquals(orders.size, orderList.size) },
            { assertEquals(oldestOrder.orderDate, orderList.toList()[0].orderDate) },
            { assertEquals(intermediateOrder.orderDate, orderList.toList()[1].orderDate) },
            { assertEquals(latestOrder.orderDate, orderList.toList()[2].orderDate) }
        )
    }

    @Test
    fun testEmpty() {
        val orderList = OrderList.of(listOf())
        assertAll(
            { assertTrue(orderList.isEmpty) },
            { assertEquals(0, orderList.size) }
        )
    }

    @ParameterizedTest
    @ArgumentsSource(LastBuyPriceProvider::class)
    fun testLastBuyPrice(orders: List<Order>, expected: Price?) {
        val orderList = OrderList.of(orders)
        assertEquals(expected, orderList.lastBuyPrice())
    }

    @Test
    fun instantiate_invalidProductCodes() {
        assertThrows<IllegalArgumentException> {
            OrderList.of(
                listOf(
                    TestOrder.createOrder(productCode = ProductCode.BTC_JPY),
                    TestOrder.createOrder(productCode = ProductCode.ETH_JPY)
                )
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OrdersProvider::class)
    fun `canSell and canBuy`(orders: List<Order>, canBuy: Boolean, canSell: Boolean) {
        val orderList = OrderList.of(orders)
        assertAll(
            { assertEquals(canBuy, orderList.canBuy()) },
            { assertEquals(canSell, orderList.canSell()) }
        )
    }

    private class OrdersProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val baseDateTime = LocalDateTime.now()
            return Stream.of(
                // orders, canBuy, canSell
                arguments(
                    listOf<OrderSignal>(),
                    true,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.ACTIVE,
                            orderDate = baseDateTime
                        )
                    ),
                    false,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.REJECTED,
                            orderDate = baseDateTime
                        )
                    ),
                    true,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        )
                    ),
                    false,
                    true
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrder(
                            orderSide = OrderSide.SELL,
                            state = OrderState.ACTIVE,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    false,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY, state = OrderState.COMPLETED, orderDate = baseDateTime
                        ),
                        TestOrder.createOrder(
                            orderSide = OrderSide.SELL,
                            state = OrderState.CANCELED,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    false,
                    true
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY, state = OrderState.COMPLETED, orderDate = baseDateTime
                        ),
                        TestOrder.createOrder(
                            orderSide = OrderSide.SELL,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    true,
                    false
                ),
            )
        }
    }

    private class LastBuyPriceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val baseDateTime = LocalDateTime.now()
            val price = Price.of(100.0)
            return Stream.of(
                arguments(
                    listOf<Order>(),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrder(orderSide = OrderSide.SELL)),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrder(orderSide = OrderSide.SELL)),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrder(orderSide = OrderSide.BUY, state = OrderState.ACTIVE)),
                    null
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price
                        )
                    ),
                    price
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price
                        )
                    ),
                    price
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = Price.of(200.0),
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price,
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    price
                ),
                arguments(
                    listOf(
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price,
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrder(
                            orderSide = OrderSide.BUY,
                            state = OrderState.ACTIVE,
                            averagePrice = Price.of(200.0),
                            orderDate = baseDateTime.plusMinutes(1)
                        )
                    ),
                    price
                ),
            )
        }
    }
}
