package org.kentunc.bittrader.order.api.infrastructure.configuration.webclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHeaderSigner
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.BodyProvidingJsonEncoder
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.ClientHttpConnectorFactory
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.MessageSigningHttpConnector
import org.kentunc.bittrader.order.api.infrastructure.configuration.properties.BitflyerLightningApiClientConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeFunctions
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(BitflyerLightningApiClientConfigurationProperties::class)
class BitflyerLightningApiClientConfiguration(private val properties: BitflyerLightningApiClientConfigurationProperties) {

    @Bean
    fun bitflyerPublicApiClient(clientHttpConnectorFactory: ClientHttpConnectorFactory): BitflyerHttpPublicApiClient {
        val connector = clientHttpConnectorFactory.create(
            properties.connectTimeout,
            properties.readTimeout
        )
        return WebClient.builder()
            .baseUrl(properties.url)
            .clientConnector(connector)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
            .let { BitflyerHttpPublicApiClient(it) }
    }

    @Bean
    fun bitflyerPrivateApiClient(
        clientHttpConnectorFactory: ClientHttpConnectorFactory,
        objectMapper: ObjectMapper
    ): BitflyerHttpPrivateApiClient {
        // NOTE: Enable hmac base authentication.
        // ref. https://andrew-flower.com/blog/Custom-HMAC-Auth-with-Spring-WebClient
        val signer = BitflyerHeaderSigner(properties.accessKey.trim(), properties.secretKey.trim())
        val connector = MessageSigningHttpConnector(signer)
        val jsonEncoder = BodyProvidingJsonEncoder(connector::signWithBody)
        val jsonDecoder = Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON)

        return WebClient.builder()
            .baseUrl(properties.url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchangeFunction(
                ExchangeFunctions.create(
                    connector,
                    ExchangeStrategies.builder()
                        .codecs {
                            it.defaultCodecs().jackson2JsonEncoder(jsonEncoder)
                            it.defaultCodecs().jackson2JsonDecoder(jsonDecoder)
                        }
                        .build()
                )
            )
            .build()
            .let { BitflyerHttpPrivateApiClient(it) }
    }
}
