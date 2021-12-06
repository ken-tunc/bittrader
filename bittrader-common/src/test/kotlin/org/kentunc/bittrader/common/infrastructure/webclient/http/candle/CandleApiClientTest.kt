package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.test.model.TestTicker
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.AutoConfigureMockWebServer
import org.kentunc.bittrader.test.webclient.MockWebServerHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime

@AutoConfigureMockWebServer(CandleApiClient::class)
internal class CandleApiClientTest {

    @Autowired
    private lateinit var helper: MockWebServerHelper

    @Autowired
    private lateinit var target: CandleApiClient

    @Test
    fun testSearch() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/candle/get_candles.json")
        helper.enqueueResponse(responseBody)
        val expected = listOf(
            CandleResponse(
                productCode = ProductCode.BTC_JPY,
                duration = Duration.DAYS,
                dateTime = LocalDateTime.of(2021, 12, 2, 0, 0),
                open = BigDecimal("6425957.5"),
                close = BigDecimal("6440994.5"),
                high = BigDecimal("6440994.5"),
                low = BigDecimal("6420203.5"),
                volume = BigDecimal("1132791.87984193")
            ),
            CandleResponse(
                productCode = ProductCode.BTC_JPY,
                duration = Duration.DAYS,
                dateTime = LocalDateTime.of(2021, 12, 4, 0, 0),
                open = BigDecimal("6008137.0"),
                close = BigDecimal("5234281.5"),
                high = BigDecimal("6015319.0"),
                low = BigDecimal("5218720.5"),
                volume = BigDecimal("143518768.5836159")
            )
        )

        val request = CandleSearchRequest(ProductCode.BTC_JPY, duration = Duration.DAYS, expected.size)

        // exercise:
        val actual = target.search(request).toList()

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { helper.assertRequest(HttpMethod.GET, "/candles?product_code=BTC_JPY&duration=DAYS&max_num=2") }
        )
    }

    @Test
    fun testFeed() = runBlocking {
        // setup:
        val request = TickerRequest.of(TestTicker.create())
        helper.enqueueResponse(status = HttpStatus.NO_CONTENT)

        // exercise:
        target.feed(request)

        // verify:
        helper.assertRequest(HttpMethod.PUT, "/candles/feed", request)
    }
}
