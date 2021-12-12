package org.kentunc.bittrader.candle.feeder.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.domain.service.CandleService
import org.kentunc.bittrader.candle.feeder.domain.service.TickerService
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleFeedInteractor::class)
internal class CandleFeedInteractorTest {

    @MockkBean(relaxed = true)
    private lateinit var tickerService: TickerService

    @MockkBean(relaxed = true)
    private lateinit var candleService: CandleService

    @Autowired
    private lateinit var target: CandleFeedInteractor

    @Test
    fun testFeedCandles() = runBlocking {
        // setup:
        val productCodes = ProductCode.values().toSet()
        val ticker = TestTicker.create()
        coEvery { tickerService.subscribe(productCodes) } returns flowOf(ticker)

        // exercise:
        target.feedCandles(productCodes)

        // verify:
        coVerify { candleService.feed(ticker) }
    }
}
