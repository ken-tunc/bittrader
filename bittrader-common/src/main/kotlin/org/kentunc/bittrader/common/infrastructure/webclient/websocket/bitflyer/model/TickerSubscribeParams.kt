package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model

data class TickerSubscribeParams(
    val channel: String,
    val message: TickerMessage
)
