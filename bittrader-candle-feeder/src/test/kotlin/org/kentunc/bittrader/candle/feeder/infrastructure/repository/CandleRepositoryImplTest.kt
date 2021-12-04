package org.kentunc.bittrader.candle.feeder.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleRepositoryImpl::class)
internal class CandleRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var candleApiClient: CandleApiClient

    @Autowired
    private lateinit var target: CandleRepositoryImpl

    @Test
    fun testFeed() = runBlocking {
        // exercise:
        target.feed(TestTicker.create())

        // verify:
        coVerify { candleApiClient.feed(any()) }
    }
}
