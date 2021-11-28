package org.kentunc.bittrader.candle.feeder.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
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
    lateinit var tickerService: TickerService

    @MockkBean(relaxed = true)
    lateinit var candleService: CandleService

    @Autowired
    lateinit var target: CandleFeedInteractor

    @Test
    fun testFeedCandles() {
        // setup:
        val btcJpyTicker = TestTicker.create(productCode = ProductCode.BTC_JPY)
        val ethJpyTicker = TestTicker.create(productCode = ProductCode.ETH_JPY)
        coEvery { tickerService.subscribe(ProductCode.BTC_JPY) } returns flowOf(btcJpyTicker)
        coEvery { tickerService.subscribe(ProductCode.ETH_JPY) } returns flowOf(ethJpyTicker)

        // exercise:
        target.feedCandles()

        // verify:
        coVerify(exactly = 1) { candleService.feed(btcJpyTicker) }
        coVerify(exactly = 1) { candleService.feed(ethJpyTicker) }
    }
}
