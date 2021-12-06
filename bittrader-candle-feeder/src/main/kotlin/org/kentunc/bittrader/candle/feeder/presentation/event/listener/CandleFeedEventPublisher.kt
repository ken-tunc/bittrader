package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("feed")
class CandleFeedEventPublisher(private val eventPublisher: ApplicationEventPublisher) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun publishEvents() {
        log.info("start feed candles...")
        ProductCode.values().forEach {
            eventPublisher.publishEvent(CandleFeedEvent(this, it))
        }
    }
}
