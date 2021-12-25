package org.kentunc.bittrader.order.api.infrastructure.repository.live

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market.CommissionRateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(LiveCommissionRateRepositoryImpl::class)
internal class LiveCommissionRateRepositoryImplTest {

    @MockkBean
    private lateinit var bitflyerClient: BitflyerHttpPrivateApiClient

    @Autowired
    private lateinit var target: LiveCommissionRateRepositoryImpl

    @Test
    fun testGet() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val commissionRate = CommissionRate.of(0.0015)
        coEvery { bitflyerClient.getCommissionRate(productCode) } returns CommissionRateResponse(commissionRate.toBigDecimal())

        // exercise:
        val actual = target.get(productCode)

        // verify:
        assertEquals(commissionRate, actual)
    }
}