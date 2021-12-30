package org.kentunc.bittrader.order.api.domain.factory

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.common.test.model.TestTicker
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import org.kentunc.bittrader.order.api.domain.repository.TickerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderFactory::class)
internal class OrderFactoryTest {

    @MockkBean
    private lateinit var balanceRepository: BalanceRepository
    @MockkBean
    private lateinit var commissionRateRepository: CommissionRateRepository
    @MockkBean
    private lateinit var tickerRepository: TickerRepository

    @Autowired
    private lateinit var target: OrderFactory

    @Test
    fun testCreateBuyAllOrder() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = TestBalance.create(currencyCode = productCode.right)
        every { balanceRepository.findAll() } returns flowOf(balance)

        val commissionRate = CommissionRate.of(0.0015)
        coEvery { commissionRateRepository.get(productCode) } returns commissionRate

        val ticker = TestTicker.create()
        coEvery { tickerRepository.get(productCode) } returns ticker

        val orderSignal = mockk<OrderSignal>()
        mockkObject(OrderSignal)
        every { OrderSignal.ofBuyAll(productCode, balance, commissionRate, ticker) } returns orderSignal

        // exercise:
        val actual = target.createBuyAllOrder(productCode)

        // verify:
        assertEquals(orderSignal, actual)
    }

    @Test
    fun testCreateSellAllOrder() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val balance = TestBalance.create(currencyCode = productCode.left)
        every { balanceRepository.findAll() } returns flowOf(balance)

        val orderSignal = mockk<OrderSignal>()
        mockkObject(OrderSignal)
        every { OrderSignal.ofSellAll(productCode, balance) } returns orderSignal

        // exercise:
        val actual = target.createSellAllOrder(productCode)

        // verify:
        assertEquals(orderSignal, actual)
    }
}
