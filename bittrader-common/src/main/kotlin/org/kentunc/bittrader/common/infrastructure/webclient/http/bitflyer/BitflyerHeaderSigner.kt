package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import org.kentunc.bittrader.common.domain.model.time.DateTimeFactory
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.HeaderSigner
import org.springframework.http.client.reactive.ClientHttpRequest
import java.net.URI
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class BitflyerHeaderSigner(private val accessKey: String, private val secretKey: String) : HeaderSigner {

    companion object {
        private const val HASH_ALGORITHM = "HmacSHA256"
        private const val HEADER_ACCESS_KEY = "ACCESS-KEY"
        private const val HEADER_ACCESS_TIMESTAMP = "ACCESS-TIMESTAMP"
        private const val HEADER_ACCESS_SIGN = "ACCESS-SIGN"
    }

    override fun injectHeader(clientRequest: ClientHttpRequest, body: ByteArray?) {
        val timestamp = DateTimeFactory.getInstant().epochSecond.toString()
        val bodyString = body?.let { String(it) } ?: ""
        val text = timestamp + clientRequest.method.toString() + buildPath(clientRequest.uri) + bodyString

        clientRequest.headers.apply {
            add(HEADER_ACCESS_KEY, accessKey)
            add(HEADER_ACCESS_TIMESTAMP, timestamp)
            add(HEADER_ACCESS_SIGN, computeHash(text))
        }
    }

    private fun buildPath(uri: URI): String {
        val path = uri.path
        val query = uri.query ?: ""
        return if (query.isEmpty()) path else "$path?$query"
    }

    private fun computeHash(text: String): String {
        val mac = Mac.getInstance(HASH_ALGORITHM).apply {
            init(SecretKeySpec(secretKey.toByteArray(), HASH_ALGORITHM))
        }
        return mac.doFinal(text.toByteArray())
            .joinToString("") { String.format("%02x", it and 255.toByte()) }
    }
}
