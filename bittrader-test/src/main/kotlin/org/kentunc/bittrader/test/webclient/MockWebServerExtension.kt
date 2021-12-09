package org.kentunc.bittrader.test.webclient

import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class MockWebServerExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        val applicationContext = getApplicationContext(context) ?: return
        val mockWebServer = applicationContext.getBean<MockWebServer>()
        applicationContext.getBean<WebClientProxy>()
            // NOTE: MockWebServer#url calls MockWebServer#start internally.
            .initialize(mockWebServer.url("/").toString())
    }

    override fun afterAll(context: ExtensionContext?) {
        getApplicationContext(context)?.getBean<MockWebServer>()?.shutdown()
    }

    private fun getApplicationContext(context: ExtensionContext?): ApplicationContext? {
        return context?.let { SpringExtension.getApplicationContext(it) }
    }
}
