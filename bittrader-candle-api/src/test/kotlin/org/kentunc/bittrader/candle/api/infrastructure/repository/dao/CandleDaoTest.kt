package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandlePrimaryKey
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import java.time.LocalDateTime

@CandleApiTest
internal class CandleDaoTest {

    @Autowired
    private lateinit var target: CandleDao

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    private val baseDateTime = LocalDateTime.of(2021, 1, 1, 12, 0, 0)
    private val candleEntities = listOf(
        CandleEntity.of(
            TestCandle.create(
                productCode = ProductCode.BTC_JPY,
                duration = Duration.MINUTES,
                dateTime = baseDateTime
            )
        ),
        CandleEntity.of(
            TestCandle.create(
                productCode = ProductCode.BTC_JPY,
                duration = Duration.MINUTES,
                dateTime = baseDateTime.plusMinutes(1)
            )
        ),
    )

    @BeforeEach
    internal fun setUp() {
        databaseClient.sql("delete from candle")
            .then()
            .block()
        candleEntities.forEach {
            databaseClient.sql("insert into candle values ($1, $2, $3, $4, $5, $6, $7, $8)")
                .bind("$1", it.productCode.toString())
                .bind("$2", it.duration.toString())
                .bind("$3", it.dateTime)
                .bind("$4", it.open)
                .bind("$5", it.close)
                .bind("$6", it.high)
                .bind("$7", it.low)
                .bind("$8", it.volume)
                .then()
                .block()
        }
    }

    @Test
    fun testSave(): Unit = runBlocking {
        val newCandle = CandleEntity.of(TestCandle.create(productCode = ProductCode.ETH_JPY))
        assertDoesNotThrow { target.insert(newCandle) }
    }

    @Test
    fun testSelectOne_hit() = runBlocking {
        // exercise:
        val actual = target.selectOne(candleEntities[0].primaryKey)

        // verify:
        assertEquals(candleEntities[0], actual)
    }

    @Test
    fun testSelectOne_no_hit() = runBlocking {
        // exercise:
        val actual =
            target.selectOne(CandlePrimaryKey(ProductCode.ETH_JPY, duration = Duration.DAYS, dateTime = baseDateTime))

        // verify:
        assertNull(actual)
    }

    @Test
    fun testSelect_hit() = runBlocking {
        // exercise:
        val query = CandleQuery(ProductCode.BTC_JPY, Duration.MINUTES, candleEntities.size)
        val actual = target.select(query).toList()

        // verify:
        assertEquals(candleEntities.reversed(), actual)
    }

    @Test
    fun testSelect_no_hit() = runBlocking {
        // exercise:
        val query = CandleQuery(ProductCode.ETH_JPY, Duration.DAYS)
        val actual = target.select(query).toList()

        // verify:
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testUpdate() = runBlocking {
        // setup:
        val updated =
            CandleEntity.of(
                TestCandle.create(
                    productCode = ProductCode.BTC_JPY,
                    duration = Duration.MINUTES,
                    dateTime = baseDateTime,
                    high = 1000.0,
                    low = 1.0,
                    open = 500.0,
                    close = 800.0,
                    volume = 200.0
                )
            )

        // exercise:
        target.update(updated)
        val actual = target.selectOne(updated.primaryKey)

        // verify:
        assertEquals(updated, actual)
    }
}
