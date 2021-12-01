package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.time.DateTimeFactory
import org.springframework.http.HttpMethod
import org.springframework.mock.http.client.reactive.MockClientHttpRequest
import java.net.URI
import java.time.Instant
import java.util.stream.Stream

internal class BitflyerHeaderSignerTest {

    companion object {
        private const val ACCESS_KEY = "access-key"
        private const val SECRET_KEY = "secret-key"
    }

    private lateinit var target: BitflyerHeaderSigner

    @BeforeEach
    internal fun setUp() {
        target = BitflyerHeaderSigner(ACCESS_KEY, SECRET_KEY)
    }

    @ParameterizedTest
    @ArgumentsSource(TestArgumentsProvider::class)
    fun testInjectHeader(requestBody: ByteArray?, expected: String) {
        // setup:
        val request = MockClientHttpRequest(HttpMethod.GET, URI("http://example.com"))
        val instant = Instant.parse("2019-04-11T05:14:12.3739915Z")
        mockkObject(DateTimeFactory)
        every { DateTimeFactory.getInstant() } returns instant

        // exercise:
        target.injectHeader(request, requestBody)

        // verify:
        assertAll(
            { assertEquals(listOf(ACCESS_KEY), request.headers["ACCESS-KEY"]) },
            { assertEquals(listOf(instant.toString()), request.headers["ACCESS-TIMESTAMP"]) },
            { assertEquals(listOf(expected), request.headers["ACCESS-SIGN"]) },
        )
    }

    private class TestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.arguments("body".toByteArray(), "9a58f8de7dd7cc25e3e56e63d9ca5b1f32636343b0ce7fc9a351fd5d51a7f45d"),
                Arguments.arguments(null, "ec092f398330e4b9bad27d7e15c4a9c422c4563fe99266c3795133f1ca49cad6")
            )
        }
    }
}
