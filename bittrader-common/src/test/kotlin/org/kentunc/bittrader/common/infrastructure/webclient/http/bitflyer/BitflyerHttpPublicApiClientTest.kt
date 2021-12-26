package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.ticker.TickerResponse
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.WebClientTest
import org.kentunc.bittrader.test.webclient.WebClientTestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@WebClientTest(BitflyerHttpPublicApiClient::class)
internal class BitflyerHttpPublicApiClientTest {

    @Autowired
    private lateinit var util: WebClientTestUtil

    @Autowired
    private lateinit var target: BitflyerHttpPublicApiClient

    @Test
    fun testGetTicker() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("mock/bitflyer/get_ticker.json")
        util.enqueueResponse(responseBody)
        val expected = TickerResponse(
            productCode = productCode,
            timestamp = LocalDateTime.of(2015, Month.JULY, 8, 2, 50, 59),
            bestBid = BigDecimal.valueOf(30000),
            bestAsk = BigDecimal.valueOf(36640),
            volume = BigDecimal("16819.26")
        )

        // exercise:
        val actual = target.getTicker(productCode)

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { util.assertRequest(HttpMethod.GET, "/ticker?product_code=$productCode") },
        )
    }
}
