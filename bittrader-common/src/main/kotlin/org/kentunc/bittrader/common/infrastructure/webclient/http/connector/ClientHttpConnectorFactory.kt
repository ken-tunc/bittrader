package org.kentunc.bittrader.common.infrastructure.webclient.http.connector

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient

class ClientHttpConnectorFactory {

    fun create(
        connectTimeoutMills: Int,
        readTimeoutMills: Int? = null
    ): ClientHttpConnector {
        return HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMills)
            .doOnConnected { connection -> readTimeoutMills?.also { connection.addHandler(ReadTimeoutHandler(it)) } }
            .let { ReactorClientHttpConnector(it) }
    }
}
