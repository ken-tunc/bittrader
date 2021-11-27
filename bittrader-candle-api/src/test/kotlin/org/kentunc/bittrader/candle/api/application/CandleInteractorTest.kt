package org.kentunc.bittrader.candle.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerifyAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleInteractor::class)
internal class CandleInteractorTest {

    @MockkBean(relaxed = true)
    lateinit var candleService: CandleService

    @Autowired
    lateinit var target: CandleInteractor

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
