package org.kentunc.bittrader.candle.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerifyAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleInteractor::class)
internal class CandleInteractorTest {

    @MockkBean(relaxed = true)
    private lateinit var candleService: CandleService

    @Autowired
    private lateinit var target: CandleInteractor

    @Test
    fun testFindLatestCandles() = runBlocking {
        // setup:
        val query = CandleQuery(productCode = ProductCode.BTC_JPY, duration = Duration.MINUTES)
        val candleList = TestCandleList.create()
        coEvery { candleService.findLatest(query) } returns candleList

        // exercise:
        val actual = target.findLatestCandles(query)

        // verify:
        assertEquals(candleList.size, actual.size)
    }

    @Test
    fun testFeedCandlesByTicker() = runBlocking {
        // setup:
        val ticker = TestTicker.create()

        // exercise:
        target.feedCandlesByTicker(ticker)

        // verify:
        coVerifyAll {
            Duration.values().forEach {
                candleService.feed(ticker, it)
            }
        }
    }
}
