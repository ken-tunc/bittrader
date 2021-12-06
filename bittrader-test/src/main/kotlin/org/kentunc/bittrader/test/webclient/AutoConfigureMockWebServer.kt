package org.kentunc.bittrader.test.webclient

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.AliasFor
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringJUnitConfig
@Import(MockWebServerConfiguration::class)
@ExtendWith(MockWebServerExtension::class)
annotation class AutoConfigureMockWebServer(
    @get:AliasFor(
        annotation = SpringJUnitConfig::class,
        attribute = "classes"
    ) vararg val values: KClass<*> = []
)
