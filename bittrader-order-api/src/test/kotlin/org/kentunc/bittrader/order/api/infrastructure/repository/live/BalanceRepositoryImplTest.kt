package org.kentunc.bittrader.order.api.infrastructure.repository.live

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market.BalanceResponse
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.order.api.infrastructure.repository.BalanceRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(BalanceRepositoryImpl::class)
internal class BalanceRepositoryImplTest {

    @MockkBean
    private lateinit var bitflyerClient: BitflyerHttpPrivateApiClient

    @Autowired
    private lateinit var target: BalanceRepositoryImpl

    @Test
    fun testFindAll() = runBlocking {
        // setup:
        val balance = TestBalance.create()
        val response = mockk<BalanceResponse>()
        every { response.toBalance() } returns balance
        every { bitflyerClient.getBalances() } returns flowOf(response)

        // exercise:
        val actual = target.findAll().toList()

        // verify:
        assertAll(
            { assertEquals(1, actual.size) },
            { assertEquals(balance, actual[0]) }
        )
    }
}
