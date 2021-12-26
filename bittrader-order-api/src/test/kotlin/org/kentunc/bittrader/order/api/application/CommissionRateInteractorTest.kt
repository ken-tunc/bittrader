package org.kentunc.bittrader.order.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.order.api.domain.service.CommissionRateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CommissionRateInteractor::class)
internal class CommissionRateInteractorTest {

    @MockkBean
    private lateinit var commissionRateService: CommissionRateService

    @Autowired
    private lateinit var target: CommissionRateInteractor

    @Test
    fun testGetByProductCode() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val commissionRate = CommissionRate.of(0.0015)
        coEvery { commissionRateService.get(productCode) } returns commissionRate

        // exercise:
        val actual = target.getByProductCode(productCode)

        // verify:
        assertEquals(commissionRate, actual)
    }
}
