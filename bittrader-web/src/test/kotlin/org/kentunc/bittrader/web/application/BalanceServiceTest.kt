package org.kentunc.bittrader.web.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.web.domain.repository.BalanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(BalanceService::class)
internal class BalanceServiceTest {

    @MockkBean
    private lateinit var balanceRepository: BalanceRepository

    @Autowired
    private lateinit var target: BalanceService

    @Test
    fun testGet() = runBlocking {
        // setup:
        val balances = flowOf(TestBalance.create())
        coEvery { balanceRepository.get() } returns balances

        // exercise:
        val actual = target.get()

        // verify:
        assertEquals(balances, actual)
    }
}
