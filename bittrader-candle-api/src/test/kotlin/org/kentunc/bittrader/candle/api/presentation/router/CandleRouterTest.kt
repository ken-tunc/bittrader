package org.kentunc.bittrader.candle.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.candle.api.application.CandleInteractor
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient

@CandleApiTest
@AutoConfigureWebTestClient
internal class CandleRouterTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean(relaxed = true)
    lateinit var candleInteractor: CandleInteractor

    @Test
    fun testFeed() {
        // setup:
        val ticker = TestTicker.create()

        // exercise & verify
        webTestClient.put()
            .uri("/candles/feed")
            .bodyValue(TickerRequest.of(ticker))
            .exchange()
            .expectStatus()
            .isNoContent

        coVerify {
            candleInteractor.feedCandlesByTicker(withArg {
                assertAll(
                    { assertEquals(ticker.id, it.id) },
                    { assertEquals(ticker.bestBid, it.bestBid) },
                    { assertEquals(ticker.bestAsk, it.bestAsk) },
                    { assertEquals(ticker.volume, it.volume) },
                )
            })
        }
    }
}
