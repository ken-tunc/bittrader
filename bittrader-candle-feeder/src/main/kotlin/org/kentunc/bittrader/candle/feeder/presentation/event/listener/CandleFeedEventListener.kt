package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.domain.exception.UnexpectedSubscribeException
import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CandleFeedEventListener(
    private val candleFeedInteractor: CandleFeedInteractor,
    private val eventPublisher: ApplicationEventPublisher
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private const val DELAY_MILLIS = 1000L
    }

    @Async
    @EventListener
    fun feedCandles(candleFeedEvent: CandleFeedEvent) = runBlocking {
        log.info("start feed candles of ${candleFeedEvent.productCode}...")
        try {
            candleFeedInteractor.feedCandles(candleFeedEvent.productCode)
        } catch (ex: UnexpectedSubscribeException) {
            log.error("unexpected subscription exception: ${ex.message}", ex)
            delay(DELAY_MILLIS)
            eventPublisher.publishEvent(CandleFeedEvent(this, candleFeedEvent.productCode))
        } finally {
            log.info("end feed candles of ${candleFeedEvent.productCode}...")
        }
    }
}
