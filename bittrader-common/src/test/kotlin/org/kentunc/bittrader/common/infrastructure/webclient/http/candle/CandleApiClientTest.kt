package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.test.model.TestTicker
import org.kentunc.bittrader.test.extension.WebClientExtension
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

internal class CandleApiClientTest {

    companion object {
        @JvmField
        @RegisterExtension
        internal val helper = WebClientExtension()
    }

    lateinit var target: CandleApiClient

    @BeforeEach
    fun setUp() {
        target = CandleApiClient(helper.createWebClient())
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
