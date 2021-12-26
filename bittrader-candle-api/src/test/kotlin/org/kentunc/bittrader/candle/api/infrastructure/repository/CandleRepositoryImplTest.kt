package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.CandleDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(CandleRepositoryImpl::class)
internal class CandleRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var candleDao: CandleDao

    @Autowired
    private lateinit var target: CandleRepositoryImpl

    @Test
    fun testFindOne_hit() = runBlocking {
        // setup:
        val candle = TestCandle.create()
        val entity = mockk<CandleEntity>()
        every { entity.toCandle() } returns candle
        coEvery { candleDao.selectOne(any()) } returns entity

        // exercise:
        val actual = target.findOne(candle.id)

        // verify:
        assertNotNull(actual)
    }

    @Test
    fun testFindOne_no_hit() = runBlocking {
        // setup:
        val candle = TestCandle.create()
        coEvery { candleDao.selectOne(any()) } returns null

        // exercise:
        val actual = target.findOne(candle.id)

        // verify:
        assertNull(actual)
    }

    @Test
    fun testFind() = runBlocking {
        // setup:
        val candle1 = TestCandle.create()
        val entity1 = mockk<CandleEntity>()
        every { entity1.toCandle() } returns candle1
        val candle2 = TestCandle.create()
        val entity2 = mockk<CandleEntity>()
        every { entity2.toCandle() } returns candle2
        coEvery { candleDao.select(any()) } returns flowOf(entity1, entity2)

        // exercise:
        val actual = target.find(CandleQuery(ProductCode.BTC_JPY, Duration.MINUTES))

        // verify:
        assertEquals(2, actual.count())
    }

    @Test
    fun testSave() = runBlocking {
        // exercise:
        target.save(TestCandle.create())

        // verify:
        coVerify { candleDao.insert(any()) }
    }

    @Test
    fun testUpdate() = runBlocking {
        // exercise:
        target.update(TestCandle.create())

        // verify:
        coVerify { candleDao.update(any()) }
    }
}
