package org.kentunc.bittrader.candle.feeder.presentation.runner

import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("feed")
class CandleFeedRunner(private val candleFeedInteractor: CandleFeedInteractor) {

    @EventListener(ApplicationReadyEvent::class)
    fun feedCandles() {
        candleFeedInteractor.feedCandles()
    }
}
