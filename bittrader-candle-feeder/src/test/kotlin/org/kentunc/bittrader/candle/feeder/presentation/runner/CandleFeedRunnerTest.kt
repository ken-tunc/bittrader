package org.kentunc.bittrader.candle.feeder.presentation.runner

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.test.CandleFeederTest
import org.springframework.test.context.ActiveProfiles

@CandleFeederTest
@ActiveProfiles("test", "feed")
internal class CandleFeedRunnerTest {

    @MockkBean(relaxed = true)
    lateinit var candleFeedInteractor: CandleFeedInteractor

    @Test
    fun testFeedCandles() {
        verify { candleFeedInteractor.feedCandles() }
    }
}
