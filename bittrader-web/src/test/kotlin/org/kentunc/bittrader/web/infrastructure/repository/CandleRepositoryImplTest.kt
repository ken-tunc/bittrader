package org.kentunc.bittrader.web.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.test.model.TestCandle
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleRepositoryImpl::class)
internal class CandleRepositoryImplTest {

    @MockkBean
    private lateinit var candleApiClient: CandleApiClient

    @Autowired
    private lateinit var target: CandleRepositoryImpl

    @Test
    fun testSearch() = runBlocking {
        // setup:
        val query = CandleQuery(ProductCode.BTC_JPY, Duration.DAYS)
        val candles = TestCandleList.create().toList()
        val candleResponse1 = mockk<CandleResponse>()
        every { candleResponse1.toCandle() } returns candles[0]
        val candleResponse2 = mockk<CandleResponse>()
        every { candleResponse2.toCandle() } returns candles[1]
        every { candleApiClient.search(any()) } returns flowOf(candleResponse1, candleResponse2)

        // exercise:
        val actual = target.search(query)

        // verify:
        assertEquals(2, actual.size)
        coVerify {
            candleApiClient.search(withArg {
                assertAll(
                    { assertEquals(query.productCode, it.productCode) },
                    { assertEquals(query.duration, it.duration) },
                    { assertEquals(query.maxNum, it.maxNum) },
                )
            })
        }
    }
}
