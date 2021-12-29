package org.kentunc.bittrader.test.webclient

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class WebClientTestUtil(private val mockWebServer: MockWebServer, private val objectMapper: ObjectMapper) {

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
        mockWebServer.enqueue(response)
    }

    fun assertRequest(method: HttpMethod, path: String, body: Any? = null) {
        val recordedRequest = mockWebServer.takeRequest()
        assertAll(
            { assertEquals(method.name, recordedRequest.method) },
            { assertEquals(path, recordedRequest.path) },
            { body ?: run { assertEquals(0, recordedRequest.bodySize) } },
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
