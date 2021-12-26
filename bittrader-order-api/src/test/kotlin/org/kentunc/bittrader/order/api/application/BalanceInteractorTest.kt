package org.kentunc.bittrader.order.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.order.api.domain.service.BalanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(BalanceInteractor::class)
internal class BalanceInteractorTest {

    @MockkBean
    private lateinit var balanceService: BalanceService

    @Autowired
    private lateinit var target: BalanceInteractor

    @Test
    fun testGetBalances() = runBlocking {
        // setup:
        val balances = flowOf(TestBalance.create())
        coEvery { balanceService.getAll() } returns balances

        // exercise:
        val actual = target.getBalances()

        // verify:
        assertEquals(balances, actual)
    }
}
