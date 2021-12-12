package org.kentunc.bittrader.web.presentation.controller

import org.junit.jupiter.api.BeforeEach
import org.kentunc.bittrader.web.test.WebApplicationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

@WebApplicationTest
internal abstract class AbstractControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    protected lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        webTestClient = MockMvcWebTestClient.bindToApplicationContext(context).build()
    }
}
