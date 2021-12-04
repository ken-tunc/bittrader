package org.kentunc.bittrader.candle.api.infrastructure.development

import org.h2.tools.Server
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

class H2ConsoleServer(private var webServer: Server) {

    companion object {
        private val log = LoggerFactory.getLogger(H2ConsoleServer::class.java)
    }

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
