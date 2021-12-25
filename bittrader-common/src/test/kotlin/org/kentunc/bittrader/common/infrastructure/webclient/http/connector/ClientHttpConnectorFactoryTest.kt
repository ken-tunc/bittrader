package org.kentunc.bittrader.common.infrastructure.webclient.http.connector

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClientHttpConnectorFactoryTest {

    private val target = ClientHttpConnectorFactory()

    @Test
    fun testCreate() {
        assertDoesNotThrow {
            target.create(1000, 3000)
        }
    }
}
