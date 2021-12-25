package org.kentunc.bittrader.order.api.infrastructure.repository.live

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market.BalanceResponse
import org.kentunc.bittrader.common.test.model.TestBalance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(LiveBalanceRepositoryImpl::class)
internal class LiveBalanceRepositoryImplTest {

    @MockkBean
    private lateinit var bitflyerClient: BitflyerHttpPrivateApiClient

    @Autowired
    private lateinit var target: LiveBalanceRepositoryImpl

    @Test
    fun testFindAll() = runBlocking {
        // setup:
        val balance = TestBalance.create()
        coEvery { bitflyerClient.getBalances() } returns flowOf(
            BalanceResponse(
                currencyCode = balance.currencyCode.toString(),
                amount = balance.amount.toBigDecimal(),
                available = balance.available.toBigDecimal()
            )
        )
        val expected = flowOf(balance).toList()

        // exercise:
        val actual = target.findAll().toList()

        // verify:
        assertAll(
            { assertEquals(expected.size, actual.size) },
            { assertEquals(expected[0].currencyCode, actual[0].currencyCode) },
            { assertEquals(expected[0].amount, actual[0].amount) },
            { assertEquals(expected[0].available, actual[0].available) }
        )
    }
}
