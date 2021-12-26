package org.kentunc.bittrader.order.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(BalanceService::class)
internal class BalanceServiceTest {

    @MockkBean
    private lateinit var balanceRepository: BalanceRepository

    @Autowired
    private lateinit var target: BalanceService

    @Test
    fun testGetAll() = runBlocking {
        // setup:
        val balances = flowOf(TestBalance.create())
        coEvery { balanceRepository.findAll() } returns balances

        // exercise:
        val actual = target.getAll()

        // verify:
        assertEquals(balances, actual)
    }
}
