package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.BalanceResponse
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.CommissionRateResponse
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.OrderRequest
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.OrderResponse
import org.kentunc.bittrader.test.extension.WebClientExtension
import org.kentunc.bittrader.test.file.ResourceReader
import org.springframework.http.HttpMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

internal class BitflyerHttpPrivateApiClientTest {

    companion object {
        @JvmField
        @RegisterExtension
        internal val helper = WebClientExtension()
    }

    lateinit var target: BitflyerHttpPrivateApiClient

    @BeforeEach
    internal fun setUp() {
        target = BitflyerHttpPrivateApiClient(helper.createWebClient())
    }

    @Test
    fun testGetBalances() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/bitflyer/get_getbalance.json")
        helper.enqueueResponse(responseBody)
        val expected = listOf(
            BalanceResponse(CurrencyCode.JPY, BigDecimal("1024078"), BigDecimal("508000")),
            BalanceResponse(CurrencyCode.BTC, BigDecimal("10.24"), BigDecimal("4.12")),
            BalanceResponse(CurrencyCode.ETH, BigDecimal("20.48"), BigDecimal("16.38"))
        )

        // exercise:
        val actual = target.getBalances().toList()

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { helper.assertRequest(HttpMethod.GET, "/me/getbalance") }
        )
    }

    @Test
    fun testGetOrders() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("mock/bitflyer/get_getchildorders.json")
        helper.enqueueResponse(responseBody)
        val expected = listOf(
            OrderResponse(
                productCode = productCode,
                side = OrderSide.BUY,
                orderType = OrderType.LIMIT,
                price = BigDecimal("30000"),
                size = BigDecimal("0.1"),
                averagePrice = BigDecimal("30000"),
                state = OrderState.COMPLETED,
                orderDate = LocalDateTime.of(2015, Month.JULY, 7, 8, 45, 53)
            ),
            OrderResponse(
                productCode = productCode,
                side = OrderSide.SELL,
                orderType = OrderType.LIMIT,
                price = BigDecimal("30000"),
                size = BigDecimal("0.1"),
                averagePrice = BigDecimal("0"),
                state = OrderState.CANCELED,
                orderDate = LocalDateTime.of(2015, Month.JULY, 7, 8, 45, 47)
            )
        )

        // exercise:
        val actual = target.getOrders(productCode).toList()

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { helper.assertRequest(HttpMethod.GET, "/me/getchildorders?product_code=$productCode") }
        )
    }

    @Test
    fun testSendOrder() = runBlocking {
        // setup:
        val request = OrderRequest(
            productCode = ProductCode.BTC_JPY,
            orderType = OrderType.LIMIT,
            side = OrderSide.BUY,
            price = BigDecimal("30000"),
            size = BigDecimal("0.1"),
            minutesToExpire = 10000,
            timeInForce = TimeInForce.GTC
        )
        val responseBody = ResourceReader.readResource("mock/bitflyer/post_sendchildorder.json")
        helper.enqueueResponse(responseBody)

        // exercise:
        target.sendOrder(request)

        // verify:
        helper.assertRequest(HttpMethod.POST, "/me/sendchildorder", request)
    }

    @Test
    fun testGetCommissionRate() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("mock/bitflyer/get_gettradingcommission.json")
        helper.enqueueResponse(responseBody)
        val expected = CommissionRateResponse(BigDecimal("0.001"))

        // exercise:
        val actual = target.getCommissionRate(productCode)

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { helper.assertRequest(HttpMethod.GET, "/me/gettradingcommission?product_code=$productCode") }
        )
    }
}
