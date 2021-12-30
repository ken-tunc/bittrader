package org.kentunc.bittrader.order.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.ticker.TickerResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(TickerRepositoryImpl::class)
internal class TickerRepositoryImplTest {

    @MockkBean
    private lateinit var bitflyerClient: BitflyerHttpPublicApiClient

    @Autowired
    private lateinit var target: TickerRepositoryImpl

    @Test
    fun testGet() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val ticker = mockk<Ticker>()
        val response = mockk<TickerResponse>()
        every { response.toTicker() } returns ticker
        coEvery { bitflyerClient.getTicker(productCode) } returns response

        // exercise:
        val actual = target.get(productCode)

        // verify:
        assertEquals(ticker, actual)
    }
}
