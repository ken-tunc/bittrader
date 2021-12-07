package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import kotlinx.coroutines.runBlocking
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CandleFeedEventListener(
    private val candleFeedInteractor: CandleFeedInteractor,
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @Async
    @EventListener
    fun feedCandles(candleFeedEvent: CandleFeedEvent) = runBlocking {
        log.info("start feed candles of ${candleFeedEvent.productCode}...")
        candleFeedInteractor.feedCandles(candleFeedEvent.productCode)
        log.info("end feed candles of ${candleFeedEvent.productCode}...")
    }
}
