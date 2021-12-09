package org.kentunc.bittrader.test.webclient

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockWebServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
internal class WebClientTestConfiguration {

    @Bean
    fun mockWebServer() = MockWebServer()

    @Bean
    fun webClientProxy() = WebClientProxy()

    @Bean
    fun webClientTestUtil() = WebClientTestUtil(
        mockWebServer(),
        jacksonObjectMapper().registerModule(JavaTimeModule())
    )
}
