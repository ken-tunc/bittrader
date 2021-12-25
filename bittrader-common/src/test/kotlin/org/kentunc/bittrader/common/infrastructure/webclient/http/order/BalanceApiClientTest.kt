package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.presentation.model.market.BalanceResponse
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.WebClientTest
import org.kentunc.bittrader.test.webclient.WebClientTestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import java.math.BigDecimal

@WebClientTest(BalanceApiClient::class)
internal class BalanceApiClientTest {

    @Autowired
    private lateinit var util: WebClientTestUtil

    @Autowired
    private lateinit var target: BalanceApiClient

    @Test
    fun testGet() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/order/get_balances.json")
        util.enqueueResponse(body = responseBody)
        val expected = listOf(
            BalanceResponse(
                currencyCode = CurrencyCode.JPY,
                amount = BigDecimal("10000.0"),
                available = BigDecimal("90000.0")
            ),
            BalanceResponse(
                currencyCode = CurrencyCode.BTC,
                amount = BigDecimal("0.00000767"),
                available = BigDecimal("0.00000767")
            ),
            BalanceResponse(
                currencyCode = CurrencyCode.ETH,
                amount = BigDecimal("0.0"),
                available = BigDecimal("0.0")
            )
        )

        // exercise:
        val actual = target.get().toList()

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { util.assertRequest(HttpMethod.GET, "/balances") }
        )
    }
}
