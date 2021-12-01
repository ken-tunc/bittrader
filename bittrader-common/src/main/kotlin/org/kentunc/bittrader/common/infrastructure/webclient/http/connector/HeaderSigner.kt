package org.kentunc.bittrader.common.infrastructure.webclient.http.connector

import org.springframework.http.client.reactive.ClientHttpRequest

interface HeaderSigner {

    fun injectHeader(clientRequest: ClientHttpRequest, body: ByteArray?)
}
