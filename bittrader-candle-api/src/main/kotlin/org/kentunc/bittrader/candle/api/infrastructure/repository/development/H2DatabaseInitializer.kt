package org.kentunc.bittrader.candle.api.infrastructure.repository.development

import kotlinx.coroutines.runBlocking
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

open class H2DatabaseInitializer(private val enumInserter: EnumInserter) {

    @EventListener(ContextRefreshedEvent::class)
    fun initialize() = runBlocking {
        enumInserter.insertEnumsIfNotPresent()
    }
}
