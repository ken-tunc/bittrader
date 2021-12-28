package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.CommissionRateApiClient
import org.kentunc.bittrader.common.presentation.model.market.CommissionRateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CommissionRateRepositoryImpl::class)
internal class CommissionRateRepositoryImplTest {

    @MockkBean
    private lateinit var commissionRateApiClient: CommissionRateApiClient

    @Autowired
    private lateinit var target: CommissionRateRepositoryImpl

    @Test
    fun testGet() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val commissionRate = CommissionRate.of(0.0015)
        val response = mockk<CommissionRateResponse>()
        every { response.toCommissionRate() } returns commissionRate
        coEvery { commissionRateApiClient.get(productCode) } returns response

        // exercise:
        val actual = target.get(productCode)

        // verify:
        assertEquals(commissionRate, actual)
    }
}
