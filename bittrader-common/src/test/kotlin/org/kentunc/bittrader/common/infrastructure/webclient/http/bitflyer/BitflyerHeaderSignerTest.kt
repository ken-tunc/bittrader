package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
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
    fun testInjectHeader(requestBody: ByteArray?, query: String?, expected: String) {
        // setup:
        val request = MockClientHttpRequest(HttpMethod.GET, URI("https://example.com/path$query"))
        val instant = Instant.parse("2019-04-11T05:14:12.3739915Z")
        mockkObject(DateTimeFactory)
        every { DateTimeFactory.getInstant() } returns instant

        // exercise:
        target.injectHeader(request, requestBody)

        // verify:
        assertAll(
            { assertEquals(listOf(ACCESS_KEY), request.headers["ACCESS-KEY"]) },
            { assertEquals(listOf(instant.epochSecond.toString()), request.headers["ACCESS-TIMESTAMP"]) },
            { assertEquals(listOf(expected), request.headers["ACCESS-SIGN"]) },
        )
    }

    private class TestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                arguments(null, null, "9f6ac73786dd6ad625cf3c88aef5f1af8911a888b8d93c3e1be7e14d158ff3c3"),
                arguments("body".toByteArray(), null, "6d178b09fbb062dd5997019ff212fba1ac386117472daf49199111939ae5d07a"),
                arguments(null, "?key=value", "eec2013ca04d93dfbb3b3af17ddb8139b3db7bad969f9876bd9545b1bce16e6c"),
                arguments("body".toByteArray(), "?key=value", "256197dba44398d4166eb8c5d18f7a74c93f015cef00ac119be4b4c176782a51"),
            )
        }
    }
}
