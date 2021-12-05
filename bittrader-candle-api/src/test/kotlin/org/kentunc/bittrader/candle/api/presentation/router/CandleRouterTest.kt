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
import org.springframework.test.web.reactive.server.returnResult
import reactor.test.StepVerifier

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
        val source = webTestClient.get()
            .uri {
                it.path("/candles")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .returnResult<CandleResponse>()
            .responseBody

        // verify:
        StepVerifier.create(source)
            .expectNextCount(candleList.size.toLong())
            .verifyComplete()

        coVerify {
            candleInteractor.findLatestCandles(withArg {
                assertAll(
                    { assertEquals(request.productCode, it.productCode) },
                    { assertEquals(request.duration, it.duration) },
                    { assertEquals(request.maxNum, it.maxNum) },
                )
            })
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
            .expectStatus().isNoContent

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
