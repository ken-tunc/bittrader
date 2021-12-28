package org.kentunc.bittrader.test.webclient

import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient
import kotlin.properties.Delegates

internal class WebClientProxy : WebClient {

    private var webClient: WebClient by Delegates.notNull()

    fun build(builder: WebClient.Builder) {
        webClient = builder.build()
    }

    override fun get() = webClient.get()

    override fun head() = webClient.head()

    override fun post() = webClient.post()

    override fun put() = webClient.put()

    override fun patch() = webClient.patch()

    override fun delete() = webClient.delete()

    override fun options() = webClient.options()

    override fun method(method: HttpMethod) = webClient.method(method)

    override fun mutate() = webClient.mutate()
}
