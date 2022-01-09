package org.kentunc.bittrader.candle.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.application.StrategyInteractor
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.kentunc.bittrader.common.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@CandleApiTest
@AutoConfigureWebTestClient
internal class StrategyRouterTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean(relaxed = true)
    private lateinit var strategyInteractor: StrategyInteractor

    @Test
    fun testGetPosition() {
        // setup:
        val request = TradePositionRequest(ProductCode.BTC_JPY, Duration.DAYS)
        val position = TradingPosition.SHOULD_BUY
        val tradingStrategy = mockk<TradingStrategy>()
        every { tradingStrategy.getPosition() } returns position
        coEvery {
            strategyInteractor.getTradingStrategy(
                request.productCode,
                request.duration
            )
        } returns tradingStrategy

        // exercise & verify:
        webTestClient.get()
            .uri {
                it.path("/strategies/positions")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { spec ->
                    spec.expectBody<TradePositionResponse>()
                        .consumeWith { assertEquals(position, it.responseBody?.position) }
                }
            )
    }

    @Test
    fun testOptimize() {
        // setup:
        val request = OptimizeRequest(ProductCode.BTC_JPY, Duration.DAYS)

        // exercise & verify:
        webTestClient.post()
            .uri("/strategies/optimize")
            .bodyValue(request)
            .exchange()
            .expectAll(
                { it.expectStatus().isNoContent },
                { it.expectBody().isEmpty }
            )

        coVerify { strategyInteractor.optimizeStrategies(request.productCode, request.duration) }
    }
}
