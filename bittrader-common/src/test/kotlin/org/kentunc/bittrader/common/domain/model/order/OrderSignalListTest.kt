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

internal class OrderSignalListTest {

    @Test
    fun testSorted() {
        // setup:
        val baseDateTime = LocalDateTime.now()
        val oldestOrder = TestOrder.createOrderSignal(orderDate = baseDateTime)
        val intermediateOrder = TestOrder.createOrderSignal(orderDate = baseDateTime.plusMinutes(1))
        val latestOrder = TestOrder.createOrderSignal(orderDate = baseDateTime.plusHours(1))
        val orders = listOf(intermediateOrder, latestOrder, oldestOrder)

        // exercise
        val orderSignalList = OrderSignalList.of(orders)

        // verify
        assertAll(
            { assertFalse(orderSignalList.isEmpty) },
            { assertEquals(orders.size, orderSignalList.size) },
            { assertEquals(oldestOrder.orderDate, orderSignalList.toList()[0].orderDate) },
            { assertEquals(intermediateOrder.orderDate, orderSignalList.toList()[1].orderDate) },
            { assertEquals(latestOrder.orderDate, orderSignalList.toList()[2].orderDate) }
        )
    }

    @Test
    fun testEmpty() {
        val orderSignalList = OrderSignalList.of(listOf())
        assertAll(
            { assertTrue(orderSignalList.isEmpty) },
            { assertEquals(0, orderSignalList.size) }
        )
    }

    @ParameterizedTest
    @ArgumentsSource(LastBuyPriceProvider::class)
    fun testLastBuyPrice(orderSignals: List<OrderSignal>, expected: Price?) {
        val orderSignalList = OrderSignalList.of(orderSignals)
        assertEquals(expected, orderSignalList.lastBuyPrice())
    }

    @Test
    fun instantiate_invalidProductCodes() {
        assertThrows<IllegalArgumentException> {
            OrderSignalList.of(
                listOf(
                    TestOrder.createOrderSignal(productCode = ProductCode.BTC_JPY),
                    TestOrder.createOrderSignal(productCode = ProductCode.ETH_JPY)
                )
            )
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OrdersProvider::class)
    fun `canSell and canBuy`(orders: List<OrderSignal>, canBuy: Boolean, canSell: Boolean) {
        val orderSignalList = OrderSignalList.of(orders)
        assertAll(
            { assertEquals(canBuy, orderSignalList.canBuy()) },
            { assertEquals(canSell, orderSignalList.canSell()) }
        )
    }

    private class OrdersProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val baseDateTime = LocalDateTime.now()
            return Stream.of(
                // orders, canBuy, canSell
                arguments(
                    listOf<Order>(),
                    true,
                    false
                ),
                arguments(
                    listOf(
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY, state = OrderState.COMPLETED, orderDate = baseDateTime
                        ),
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY, state = OrderState.COMPLETED, orderDate = baseDateTime
                        ),
                        TestOrder.createOrderSignal(
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
                    listOf<OrderSignal>(),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrderSignal(orderSide = OrderSide.SELL)),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrderSignal(orderSide = OrderSide.SELL)),
                    null
                ),
                arguments(
                    listOf(TestOrder.createOrderSignal(orderSide = OrderSide.BUY, state = OrderState.ACTIVE)),
                    null
                ),
                arguments(
                    listOf(
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price
                        )
                    ),
                    price
                ),
                arguments(
                    listOf(
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price
                        )
                    ),
                    price
                ),
                arguments(
                    listOf(
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = Price.of(200.0),
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrderSignal(
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
                        TestOrder.createOrderSignal(
                            orderSide = OrderSide.BUY,
                            state = OrderState.COMPLETED,
                            averagePrice = price,
                            orderDate = baseDateTime
                        ),
                        TestOrder.createOrderSignal(
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
