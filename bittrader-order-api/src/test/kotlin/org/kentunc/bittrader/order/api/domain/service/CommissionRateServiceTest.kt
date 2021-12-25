package org.kentunc.bittrader.order.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CommissionRateService::class)
internal class CommissionRateServiceTest {

    @MockkBean
    private lateinit var commissionRateRepository: CommissionRateRepository

    @Autowired
    private lateinit var target: CommissionRateService

    @Test
    fun testGet() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val commissionRate = CommissionRate.of(0.0015)
        coEvery { commissionRateRepository.get(productCode) } returns commissionRate

        // exercise:
        val actual = target.get(productCode)

        // verify:
        assertEquals(commissionRate, actual)
    }
}
