package org.kentunc.bittrader.test.webclient

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockWebServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@TestConfiguration
class MockWebServerConfiguration {

    @Bean
    fun mockWebServer() = MockWebServer().apply {
        start(0)
    }

    @Bean
    fun webClient() = WebClient.builder()
        .baseUrl(mockWebServer().url("/").toUrl().toString())
        .build()

    @Bean
    fun mockWebServerHelper() = MockWebServerHelper(
        mockWebServer(),
        jacksonObjectMapper().registerModule(JavaTimeModule())
    )
}
