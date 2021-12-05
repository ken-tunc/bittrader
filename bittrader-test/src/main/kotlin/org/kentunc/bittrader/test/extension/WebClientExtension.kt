package org.kentunc.bittrader.test.extension

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class WebClientExtension : BeforeAllCallback, AfterAllCallback {

    companion object {
        private val objectMapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
    }

    private lateinit var mockServer: MockWebServer

    override fun beforeAll(context: ExtensionContext?) {
        mockServer = MockWebServer()
        mockServer.start(0)
    }

    override fun afterAll(context: ExtensionContext?) {
        mockServer.shutdown()
    }

    fun createWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(mockServer.url("/").toUrl().toString())
            .build()
    }

    fun enqueueResponse(
        body: String? = null,
        status: HttpStatus = HttpStatus.OK,
        headers: HttpHeaders = HttpHeaders.EMPTY
    ) {
        val response = MockResponse().apply {
            body?.also {
                setBody(body)
            }
            setResponseCode(status.value())

            headers.forEach { key, value -> addHeader(key, value) }
            addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }
        mockServer.enqueue(response)
    }

    fun assertRequest(method: HttpMethod, path: String, body: Any? = null) {
        val recordedRequest = mockServer.takeRequest()
        assertAll(
            { assertEquals(method.name, recordedRequest.method) },
            { assertEquals(path, recordedRequest.path) },
            {
                body?.also {
                    val expected = objectMapper.writeValueAsString(body)
                    val actual = recordedRequest.body.readUtf8()
                    assertEquals(expected, actual)
                }
            }
        )
    }
}
