package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.test.model.TestCandle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient

@CandleApiTest
internal class CandleDaoTest {

    @Autowired
    private lateinit var target: CandleDao

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    private val candleEntity = CandleEntity.of(TestCandle.create())

    @BeforeEach
    internal fun setUp() {
        databaseClient.sql("delete from candle")
            .then()
            .block()
        databaseClient.sql("insert into candle values ($1, $2, $3, $4, $5, $6, $7, $8)")
            .bind("$1", candleEntity.productCode.toString())
            .bind("$2", candleEntity.duration.toString())
            .bind("$3", candleEntity.dateTime)
            .bind("$4", candleEntity.open)
            .bind("$5", candleEntity.close)
            .bind("$6", candleEntity.high)
            .bind("$7", candleEntity.low)
            .bind("$8", candleEntity.volume)
            .then()
            .block()
    }

    @Test
    fun testSave(): Unit = runBlocking {
        val newCandle = CandleEntity.of(TestCandle.create(productCode = ProductCode.ETH_JPY))
        assertDoesNotThrow { target.insert(newCandle) }
    }

    @Test
    fun testFind() = runBlocking {
        // exercise:
        val actual = target.selectOne(candleEntity.primaryKey)

        // verify:
        assertEquals(candleEntity, actual)
    }

    @Test
    fun testUpdate() = runBlocking {
        // setup:
        val updated =
            CandleEntity.of(TestCandle.create(high = 1000.0, low = 1.0, open = 500.0, close = 800.0, volume = 200.0))

        // exercise:
        target.update(updated)
        val actual = target.selectOne(updated.primaryKey)

        // verify:
        assertEquals(updated, actual)
    }
}
