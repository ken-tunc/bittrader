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
        val request = MockClientHttpRequest(HttpMethod.GET, URI("http://example.com/path"))
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
                Arguments.arguments("body".toByteArray(), "4602d18bbc0a55d7d33cb139bb5d537d9aaed05bf5e86c2f8fbce55783304fbc"),
                Arguments.arguments(null, "c6a885eb3713485aeead5ca2003258bbb47330a8229fa0186fd8e29587ed2b23")
            )
        }
    }
}
