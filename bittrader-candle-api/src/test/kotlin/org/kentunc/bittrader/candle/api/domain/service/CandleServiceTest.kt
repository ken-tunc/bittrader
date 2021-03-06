package org.kentunc.bittrader.candle.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.repository.CandleRepository
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandle
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.kentunc.bittrader.common.test.model.TestTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.time.LocalDateTime

@SpringJUnitConfig(CandleService::class)
internal class CandleServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var candleRepository: CandleRepository

    @Autowired
    private lateinit var target: CandleService

    @Test
    fun testFindLatest() = runBlocking {
        // setup:
        val query = CandleQuery(productCode = ProductCode.BTC_JPY, duration = Duration.MINUTES)
        val candleList = TestCandleList.create()
        coEvery { candleRepository.find(query) } returns candleList.toList().asFlow()

        // exercise:
        val actual = target.findLatest(query)

        // verify:
        assertEquals(candleList.size, actual.size)
    }

    @Test
    fun testFeed_save() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val duration = Duration.DAYS

        val newCandle = TestCandle.create()
        mockkObject(Candle)
        every { Candle.of(ticker, duration) } returns newCandle

        coEvery { candleRepository.findOne(newCandle.id) } returns null

        // exercise:
        target.feed(ticker, duration)

        // verify:
        coVerify { candleRepository.save(newCandle) }
    }

    @Test
    fun testFeed_update() = runBlocking {
        // setup:
        val ticker = TestTicker.create()
        val duration = Duration.DAYS

        val newCandle = TestCandle.create(dateTime = LocalDateTime.now())
        mockkObject(Candle)
        every { Candle.of(ticker, duration) } returns newCandle

        val matched = TestCandle.create(dateTime = LocalDateTime.now())
        coEvery { candleRepository.findOne(newCandle.id) } returns matched

        // exercise:
        target.feed(ticker, duration)

        // verify:
        coVerify {
            candleRepository.update(
                withArg {
                    assertEquals(matched.id, it.id)
                }
            )
        }
    }
}
