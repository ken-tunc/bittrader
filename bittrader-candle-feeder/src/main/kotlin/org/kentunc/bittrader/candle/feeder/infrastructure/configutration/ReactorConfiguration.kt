package org.kentunc.bittrader.candle.feeder.infrastructure.configutration

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Hooks
import javax.annotation.PostConstruct

@Configuration
class ReactorConfiguration {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @PostConstruct
    fun hookLogsOnErrorDropped() {
        Hooks.onErrorDropped {
            log.error("an error dropped.", it)
        }
    }
}
