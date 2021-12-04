package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.CandleDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
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
    fun testFind_hit() = runBlocking {
        // setup:
        val candle = TestCandle.create()
        val entity = mockk<CandleEntity>()
        every { entity.toCandle() } returns candle
        coEvery { candleDao.find(any()) } returns entity

        // exercise:
        val actual = target.find(candle.id)

        // verify:
        assertNotNull(actual)
    }

    @Test
    fun testFind_no_hit() = runBlocking {
        // setup:
        val candle = TestCandle.create()
        coEvery { candleDao.find(any()) } returns null

        // exercise:
        val actual = target.find(candle.id)

        // verify:
        assertNull(actual)
    }

    @Test
    fun testSave() = runBlocking {
        // exercise:
        target.save(TestCandle.create())

        // verify:
        coVerify { candleDao.save(any()) }
    }

    @Test
    fun testUpdate() = runBlocking {
        // exercise:
        target.update(TestCandle.create())

        // verify:
        coVerify { candleDao.update(any()) }
    }
}
