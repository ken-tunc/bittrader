package org.kentunc.bittrader.web.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.kentunc.bittrader.web.domain.repository.CandleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleService::class)
internal class CandleServiceTest {

    @MockkBean
    private lateinit var candleRepository: CandleRepository

    @Autowired
    private lateinit var target: CandleService

    @Test
    fun testSearch() = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        coEvery { candleRepository.search(any()) } returns candleList

        // exercise:
        val actual = target.search(CandleQuery(ProductCode.BTC_JPY, Duration.DAYS))

        // verify:
        assertEquals(candleList.size, actual.size)
    }
}
