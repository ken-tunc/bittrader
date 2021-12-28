package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.BalanceApiClient
import org.kentunc.bittrader.common.presentation.model.market.BalanceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.math.BigDecimal

@SpringJUnitConfig(BalanceRepositoryImpl::class)
internal class BalanceRepositoryImplTest {

    @MockkBean
    private lateinit var balanceApiClient: BalanceApiClient

    @Autowired
    private lateinit var target: BalanceRepositoryImpl

    @Test
    fun testGet_hit() = runBlocking {
        // setup:
        val currencyCode = CurrencyCode.JPY
        val balanceResponse = BalanceResponse(
            currencyCode = currencyCode,
            amount = BigDecimal.valueOf(100.0),
            available = BigDecimal.valueOf(100.0)
        )
        every { balanceApiClient.get() } returns flowOf(balanceResponse)

        // exercise:
        val actual = target.get(currencyCode)

        // verify:
        assertEquals(balanceResponse.currencyCode, actual?.currencyCode)
    }

    @Test
    fun testGet_noHit() = runBlocking {
        // setup:
        val currencyCode = CurrencyCode.BTC
        val balanceResponse = BalanceResponse(
            currencyCode = CurrencyCode.ETH,
            amount = BigDecimal.valueOf(100.0),
            available = BigDecimal.valueOf(100.0)
        )
        every { balanceApiClient.get() } returns flowOf(balanceResponse)

        // exercise:
        val actual = target.get(currencyCode)

        // verify:
        assertNull(actual)
    }
}
