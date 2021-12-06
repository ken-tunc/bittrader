package org.kentunc.bittrader.test.webclient

import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.beans.factory.getBean
import org.springframework.test.context.junit.jupiter.SpringExtension

class MockWebServerExtension : AfterAllCallback {

    override fun afterAll(context: ExtensionContext?) {
        context?.also {
            SpringExtension.getApplicationContext(context)
                .getBean<MockWebServer>()
                .shutdown()
        }
    }
}
