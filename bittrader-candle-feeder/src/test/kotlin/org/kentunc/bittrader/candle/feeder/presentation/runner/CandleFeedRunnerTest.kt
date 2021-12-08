package org.kentunc.bittrader.candle.feeder.presentation.runner

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.test.CandleFeederTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles

@CandleFeederTest
@ActiveProfiles("test", "feed")
internal class CandleFeedRunnerTest {

    @MockkBean(relaxed = true)
    private lateinit var candleFeedInteractor: CandleFeedInteractor

    @Autowired
    private lateinit var target: CandleFeedRunner

    @Test
    fun testRun() {
        target.run(mockk())
        coVerify { candleFeedInteractor.feedCandles(ProductCode.values().toSet()) }
    }
}
