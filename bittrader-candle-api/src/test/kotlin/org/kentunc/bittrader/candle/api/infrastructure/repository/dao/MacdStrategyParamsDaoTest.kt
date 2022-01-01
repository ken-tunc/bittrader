package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.MacdParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient

@CandleApiTest
internal class MacdStrategyParamsDaoTest {

    @Autowired
    private lateinit var target: StrategyParamsDao

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    private val macdParamsEntities = listOf(
        // old
        MacdParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            shortTimeFrame = 10,
            longTimeFrame = 20,
            signalTimeFrame = 6
        ),
        // latest
        MacdParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            shortTimeFrame = 9,
            longTimeFrame = 18,
            signalTimeFrame = 7
        ),
    )

    @BeforeEach
    fun setUp() {
        databaseClient.sql("delete from macd_params")
            .then()
            .block()
        macdParamsEntities.forEach {
            databaseClient.sql("insert into macd_params (product_code, duration, short_time_frame, long_time_frame, signal_time_frame) values ($1, $2, $3, $4, $5)")
                .bind("$1", it.productCode.toString())
                .bind("$2", it.duration.toString())
                .bind("$3", it.shortTimeFrame)
                .bind("$4", it.longTimeFrame)
                .bind("$5", it.signalTimeFrame)
                .then()
                .block()
        }
    }

    @Test
    fun testSelectLatestOne_notHit() = runBlocking {
        val actual = target.selectLatestOne<MacdParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.MINUTES))
        assertNull(actual)
    }

    @Test
    fun testSelectLatestOne_hit() = runBlocking {
        val actual = target.selectLatestOne<MacdParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.DAYS))
        assertEquals(macdParamsEntities.last(), actual)
    }

    @Test
    fun testInsert(): Unit = runBlocking {
        val entity = MacdParamsEntity(
            productCode = ProductCode.ETH_JPY,
            duration = Duration.HOURS,
            shortTimeFrame = 8,
            longTimeFrame = 15,
            signalTimeFrame = 9
        )
        assertDoesNotThrow { target.insert(entity) }
    }
}
