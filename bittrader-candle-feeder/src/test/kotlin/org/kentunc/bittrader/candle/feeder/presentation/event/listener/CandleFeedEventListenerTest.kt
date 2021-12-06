package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.domain.exception.UnexpectedSubscribeException
import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.ApplicationEventPublisher

internal class CandleFeedEventListenerTest {

    private val runner = ApplicationContextRunner()

    @Test
    fun testFeedCandles() {
        // setup:
        val candleFeedInteractor = mockk<CandleFeedInteractor>(relaxed = true)
        val eventPublisher = mockk<ApplicationEventPublisher>()
        val productCode = ProductCode.BTC_JPY

        // exercise & verify:
        runner.withBean(CandleFeedEventListener::class.java, candleFeedInteractor, eventPublisher)
            .run {
                it.publishEvent(CandleFeedEvent(this, productCode))
                coVerify { candleFeedInteractor.feedCandles(productCode) }
            }
    }

    @Test
    fun testFeedCandles_thrown() {
        // setup:
        val candleFeedInteractor = mockk<CandleFeedInteractor>()
        coEvery { candleFeedInteractor.feedCandles(any()) } throws UnexpectedSubscribeException("test")
        val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
        val productCode = ProductCode.BTC_JPY

        // exercise & verify:
        runner.withBean(CandleFeedEventListener::class.java, candleFeedInteractor, eventPublisher)
            .run {
                it.publishEvent(CandleFeedEvent(this, productCode))
                verify { eventPublisher.publishEvent(any()) }
            }
    }
}
