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
    lateinit var tickerService: TickerService

    @MockkBean(relaxed = true)
    lateinit var candleService: CandleService

    @Autowired
    lateinit var target: CandleFeedInteractor

    @Test
    fun testFeedCandles() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val ticker = TestTicker.create(productCode = productCode)
        coEvery { tickerService.subscribe(ProductCode.BTC_JPY) } returns flowOf(ticker)

        // exercise:
        target.feedCandles(productCode)

        // verify:
        coVerify { candleService.feed(ticker) }
    }
}
