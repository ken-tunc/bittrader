package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.boot.test.context.runner.ApplicationContextRunner

internal class CandleFeedEventListenerTest {

    private val runner = ApplicationContextRunner()

    @Test
    fun testFeedCandles() {
        // setup:
        val candleFeedInteractor = mockk<CandleFeedInteractor>(relaxed = true)
        val productCode = ProductCode.BTC_JPY

        // exercise & verify:
        runner.withBean(CandleFeedEventListener::class.java, candleFeedInteractor)
            .run {
                it.publishEvent(CandleFeedEvent(this, productCode))
                coVerify { candleFeedInteractor.feedCandles(productCode) }
            }
    }
}
