package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.BBandsParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient

@CandleApiTest
internal class BBandsStrategyParamsDaoTest {

    @Autowired
    private lateinit var target: StrategyParamsDao

    @Autowired
    private lateinit var databaseClient: DatabaseClient

    private val bBandsParamsEntities = listOf(
        // old
        BBandsParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            timeFrame = 20,
            buyK = 2.0,
            sellK = 1.0,
        ),
        // latest
        BBandsParamsEntity(
            productCode = ProductCode.BTC_JPY,
            duration = Duration.DAYS,
            timeFrame = 18,
            buyK = 1.8,
            sellK = 0.8,
        ),
    )

    @BeforeEach
    fun setUp() {
        databaseClient.sql("delete from bbands_params")
            .then()
            .block()
        bBandsParamsEntities.forEach {
            databaseClient.sql("insert into bbands_params (product_code, duration, time_frame, buy_k, sell_k) values ($1, $2, $3, $4, $5)")
                .bind("$1", it.productCode.toString())
                .bind("$2", it.duration.toString())
                .bind("$3", it.timeFrame)
                .bind("$4", it.buyK)
                .bind("$5", it.sellK)
                .then()
                .block()
        }
    }

    @Test
    fun testSelectLatestOne_notHit() = runBlocking {
        val actual =
            target.selectLatestOne<BBandsParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.MINUTES))
        assertNull(actual)
    }

    @Test
    fun testSelectLatestOne_hit() = runBlocking {
        val actual =
            target.selectLatestOne<BBandsParamsEntity>(StrategyParamsPrimaryKey(ProductCode.BTC_JPY, Duration.DAYS))
        assertEquals(bBandsParamsEntities.last(), actual)
    }

    @Test
    fun testInsert(): Unit = runBlocking {
        val entity = BBandsParamsEntity(
            productCode = ProductCode.ETH_JPY,
            duration = Duration.HOURS,
            timeFrame = 22,
            buyK = 2.2,
            sellK = 1.2
        )
        assertDoesNotThrow { target.insert(entity) }
    }
}
