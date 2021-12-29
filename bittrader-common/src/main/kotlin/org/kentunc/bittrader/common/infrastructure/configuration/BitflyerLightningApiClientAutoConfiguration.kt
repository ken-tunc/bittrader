package org.kentunc.bittrader.common.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHeaderSigner
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.BodyProvidingJsonEncoder
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.ClientHttpConnectorFactory
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.MessageSigningHttpConnector
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
@ConditionalOnProperty(prefix = "bittrader.external.bitflyer.http", name = ["url", "access-key", "secret-key"])
@ConditionalOnBean(ObjectMapper::class)
class BitflyerLightningApiClientAutoConfiguration(
    private val properties: BitflyerLightningApiClientConfigurationProperties,
    private val clientHttpConnectorFactory: ClientHttpConnectorFactory
) {

    @Bean
    fun bitflyerPublicApiClient(): BitflyerHttpPublicApiClient {
        val connector = clientHttpConnectorFactory.create(properties.connectTimeout, properties.readTimeout)
        return WebClient.builder()
            .baseUrl(properties.url)
            .clientConnector(connector)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
            .let { BitflyerHttpPublicApiClient(it) }
    }

    @Bean
    fun bitflyerPrivateApiClient(objectMapper: ObjectMapper): BitflyerHttpPrivateApiClient {
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
