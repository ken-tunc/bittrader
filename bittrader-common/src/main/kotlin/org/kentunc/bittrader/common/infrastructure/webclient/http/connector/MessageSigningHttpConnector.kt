package org.kentunc.bittrader.common.infrastructure.webclient.http.connector

import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ClientHttpRequest
import org.springframework.http.client.reactive.ClientHttpResponse
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Function

class MessageSigningHttpConnector(private val signer: HeaderSigner) : ReactorClientHttpConnector() {

    companion object {
        private val BODYLESS_METHODS = setOf(
            HttpMethod.GET,
            HttpMethod.DELETE,
            HttpMethod.TRACE,
            HttpMethod.HEAD,
            HttpMethod.OPTIONS
        )
    }

    private val request: ThreadLocal<ClientHttpRequest> = ThreadLocal()

    override fun connect(
        method: HttpMethod,
        uri: URI,
        requestCallback: Function<in ClientHttpRequest, Mono<Void>>
    ): Mono<ClientHttpResponse> {
        return super.connect(method, uri) {
            sign(it)
            requestCallback.apply(it)
        }
    }

    private fun sign(request: ClientHttpRequest) {
        when (request.method) {
            in BODYLESS_METHODS -> signer.injectHeader(request, null)
            else -> this.request.set(request)
        }
    }

    fun signWithBody(bodyData: ByteArray) {
        signer.injectHeader(request.get(), bodyData)
        request.remove()
    }
}
