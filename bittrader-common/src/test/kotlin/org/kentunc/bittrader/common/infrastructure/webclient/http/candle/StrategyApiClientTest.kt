package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.WebClientTest
import org.kentunc.bittrader.test.webclient.WebClientTestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@WebClientTest(StrategyApiClient::class)
internal class StrategyApiClientTest {

    @Autowired
    private lateinit var util: WebClientTestUtil

    @Autowired
    private lateinit var target: StrategyApiClient

    @Test
    fun testGetPosition() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/candle/get_strategies_positions.json")
        util.enqueueResponse(body = responseBody)
        val expected = TradePositionResponse(TradingPosition.NEUTRAL)

        val request = TradePositionRequest(ProductCode.BTC_JPY, Duration.DAYS)

        // exercise:
        val actual = target.getPosition(request)

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { util.assertRequest(HttpMethod.GET, "/strategies/positions?product_code=BTC_JPY&duration=DAYS") }
        )
    }

    @Test
    fun testOptimize() = runBlocking {
        // setup:
        val request = OptimizeRequest(ProductCode.BTC_JPY, Duration.DAYS)
        util.enqueueResponse(status = HttpStatus.NO_CONTENT)

        // exercise:
        target.optimize(request)

        // verify:
        util.assertRequest(HttpMethod.POST, "/strategies/optimize", request)
    }
}
