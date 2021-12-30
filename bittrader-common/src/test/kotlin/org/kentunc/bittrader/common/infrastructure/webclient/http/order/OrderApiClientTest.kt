package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.presentation.model.order.OrderDetailDto
import org.kentunc.bittrader.common.presentation.model.order.OrderResponse
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.WebClientTest
import org.kentunc.bittrader.test.webclient.WebClientTestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@WebClientTest(OrderApiClient::class)
internal class OrderApiClientTest {

    @Autowired
    private lateinit var util: WebClientTestUtil

    @Autowired
    private lateinit var target: OrderApiClient

    @Test
    fun testGet() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/order/get_orders_product_code.json")
        util.enqueueResponse(body = responseBody)
        val expected = listOf(
            OrderResponse(
                detail = OrderDetailDto(
                    productCode = ProductCode.BTC_JPY,
                    orderType = OrderType.MARKET,
                    orderSide = OrderSide.BUY,
                    size = BigDecimal("0.001"),
                    price = null
                ),
                averagePrice = BigDecimal("2874417.0"),
                state = OrderState.COMPLETED,
                orderDate = LocalDateTime.of(2020, Month.DECEMBER, 30, 4, 12, 42)
            ),
            OrderResponse(
                detail = OrderDetailDto(
                    productCode = ProductCode.BTC_JPY,
                    orderType = OrderType.MARKET,
                    orderSide = OrderSide.SELL,
                    size = BigDecimal("0.001"),
                    price = null
                ),
                averagePrice = BigDecimal("2870002.0"),
                state = OrderState.COMPLETED,
                orderDate = LocalDateTime.of(2020, Month.DECEMBER, 30, 4, 16, 57)
            ),
            OrderResponse(
                detail = OrderDetailDto(
                    productCode = ProductCode.BTC_JPY,
                    orderType = OrderType.LIMIT,
                    orderSide = OrderSide.BUY,
                    size = BigDecimal("0.001"),
                    price = BigDecimal("2854953.0")
                ),
                averagePrice = BigDecimal("2852132.0"),
                state = OrderState.COMPLETED,
                orderDate = LocalDateTime.of(2020, Month.DECEMBER, 30, 9, 9, 15)
            ),
            OrderResponse(
                detail = OrderDetailDto(
                    productCode = ProductCode.BTC_JPY,
                    orderType = OrderType.LIMIT,
                    orderSide = OrderSide.SELL,
                    size = BigDecimal("0.001"),
                    price = BigDecimal("2854953.0")
                ),
                averagePrice = BigDecimal("2854953.0"),
                state = OrderState.COMPLETED,
                orderDate = LocalDateTime.of(2020, Month.DECEMBER, 30, 9, 9, 32)
            )
        )

        // exercise:
        val actual = target.get(ProductCode.BTC_JPY).toList()

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { util.assertRequest(HttpMethod.GET, "/orders/BTC_JPY") }
        )
    }

    @Test
    fun testSend() = runBlocking {
        // setup:
        val request = OrderSignalRequest(ProductCode.BTC_JPY, OrderSide.BUY)
        util.enqueueResponse(status = HttpStatus.NO_CONTENT)

        // exercise:
        target.send(request)

        // verify:
        util.assertRequest(HttpMethod.POST, "/orders", request)
    }
}
