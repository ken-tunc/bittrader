package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.RsiParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient

@CandleApiTest
internal class RsiStrategyParamsDaoTest {

    @Autowired
    private lateinit var target: StrategyParamsDao

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    private val rsiParamsEntities = listOf(
        // old
        RsiParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            timeFrame = 14,
            buyThreshold = 20,
            sellThreshold = 80,
        ),
        // latest
        RsiParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            timeFrame = 15,
            buyThreshold = 21,
            sellThreshold = 82,
        ),
    )

    @BeforeEach
    fun setUp() {
        databaseClient.sql("delete from rsi_params")
            .then()
            .block()
        rsiParamsEntities.forEach {
            databaseClient.sql("insert into rsi_params (product_code, duration, time_frame, buy_threshold, sell_threshold) values ($1, $2, $3, $4, $5)")
                .bind("$1", it.productCode.toString())
                .bind("$2", it.duration.toString())
                .bind("$3", it.timeFrame)
                .bind("$4", it.buyThreshold)
                .bind("$5", it.sellThreshold)
                .then()
                .block()
        }
    }

    @Test
    fun testSelectLatestOne_notHit() = runBlocking {
        val actual =
            target.selectLatestOne<RsiParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.MINUTES))
        assertNull(actual)
    }

    @Test
    fun testSelectLatestOne_hit() = runBlocking {
        val actual =
            target.selectLatestOne<RsiParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.DAYS))
        assertEquals(rsiParamsEntities.last(), actual)
    }

    @Test
    fun testInsert(): Unit = runBlocking {
        val entity = RsiParamsEntity(
            productCode = ProductCode.ETH_JPY,
            duration = Duration.HOURS,
            timeFrame = 8,
            buyThreshold = 15,
            sellThreshold = 85
        )
        assertDoesNotThrow { target.insert(entity) }
    }
}
