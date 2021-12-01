package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.TickerResponse
import org.kentunc.bittrader.test.extension.WebClientExtension
import org.kentunc.bittrader.test.file.ResourceReader
import org.springframework.http.HttpMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

internal class BitflyerHttpPublicApiClientTest {

    companion object {
        @JvmField
        @RegisterExtension
        internal val helper = WebClientExtension()
    }

    lateinit var target: BitflyerHttpPublicApiClient

    @BeforeEach
    internal fun setUp() {
        target = BitflyerHttpPublicApiClient(helper.createWebClient())
    }

    @Test
    fun testGetTicker() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val responseBody = ResourceReader.readResource("mock/bitflyer/get_ticker.json")
        helper.enqueueResponse(responseBody)
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
            { helper.assertRequest(HttpMethod.GET, "/ticker?product_code=$productCode")},
        )
    }
}
