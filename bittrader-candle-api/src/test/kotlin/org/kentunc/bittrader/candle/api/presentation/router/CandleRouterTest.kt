package org.kentunc.bittrader.candle.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.candle.api.application.CandleInteractor
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@CandleApiTest
@AutoConfigureWebTestClient
internal class CandleRouterTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean(relaxed = true)
    private lateinit var candleInteractor: CandleInteractor

    @Test
    fun testSearch() {
        // setup:
        val request = CandleSearchRequest(ProductCode.BTC_JPY, Duration.MINUTES, 100)
        val candleList = TestCandleList.create()
        coEvery { candleInteractor.findLatestCandles(any()) } returns candleList

        // exercise:
        webTestClient.get()
            .uri {
                it.path("/candles")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { it.expectBodyList<CandleResponse>().hasSize(candleList.size) }
            )

        coVerify {
            candleInteractor.findLatestCandles(
                withArg {
                    assertAll(
                        { assertEquals(request.productCode, it.productCode) },
                        { assertEquals(request.duration, it.duration) },
                        { assertEquals(request.maxNum, it.maxNum) },
                    )
                }
            )
        }
    }

    @Test
    fun testFeed() {
        // setup:
        val ticker = TestTicker.create()

        // exercise & verify:
        webTestClient.put()
            .uri("/candles/feed")
            .bodyValue(TickerRequest.of(ticker))
            .exchange()
            .expectAll(
                { it.expectStatus().isNoContent },
                { it.expectBody().isEmpty }
            )

        coVerify {
            candleInteractor.feedCandlesByTicker(
                withArg {
                    assertAll(
                        { assertEquals(ticker.id, it.id) },
                        { assertEquals(ticker.bestBid, it.bestBid) },
                        { assertEquals(ticker.bestAsk, it.bestAsk) },
                        { assertEquals(ticker.volume, it.volume) },
                    )
                }
            )
        }
    }
}
