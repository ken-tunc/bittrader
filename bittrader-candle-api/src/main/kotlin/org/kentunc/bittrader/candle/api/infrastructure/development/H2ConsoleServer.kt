package org.kentunc.bittrader.candle.api.infrastructure.development

import org.h2.tools.Server
import org.kentunc.bittrader.common.shared.extension.log
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

class H2ConsoleServer(private var webServer: Server) {

    @EventListener(ContextRefreshedEvent::class)
    fun start() {
        webServer = webServer.start()
        log.info("Start h2 console: ${webServer.url}")
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        webServer.stop()
    }
}
