package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.RsiConfiguration
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.RsiConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.RsiParamsEntity
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiThreshold
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [RsiConfiguration::class], initializers = [ConfigDataApplicationContextInitializer::class])
internal class RsiParamsRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var strategyParamsDao: StrategyParamsDao

    @Autowired
    private lateinit var properties: RsiConfigurationProperties

    @Autowired
    private lateinit var target: RsiParamsRepositoryImpl

    @Test
    fun testGet_default() = runBlocking {
        // setup:
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        coEvery { strategyParamsDao.selectLatestOne<RsiParamsEntity>(any()) } returns null
        val expected = StrategyValues.of(
            id,
            RsiParams(
                timeFrame = TimeFrame.of(properties.defaultTimeFrame),
                buyThreshold = RsiThreshold.of(properties.defaultBuyThreshold),
                sellThreshold = RsiThreshold.of(properties.defaultSellThreshold)
            )
        )

        // exercise:
        val actual = target.get(id)

        // verify:
        assertAll(
            { assertEquals(expected.id, actual.id) },
            { assertEquals(expected.params, actual.params) }
        )
        coVerify {
            strategyParamsDao.selectLatestOne<RsiParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) }
                    )
                }
            )
        }
    }

    @Test
    fun testGet_stored() = runBlocking {
        // setup:
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val params = RsiParams(
            timeFrame = TimeFrame.of(10),
            buyThreshold = RsiThreshold.of(25),
            sellThreshold = RsiThreshold.of(85)
        )
        coEvery { strategyParamsDao.selectLatestOne<RsiParamsEntity>(any()) } returns RsiParamsEntity.of(id, params)

        // exercise:
        val actual = target.get(id)

        // verify:
        assertAll(
            { assertEquals(id, actual.id) },
            { assertEquals(params, actual.params) }
        )
        coVerify {
            strategyParamsDao.selectLatestOne<RsiParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) }
                    )
                }
            )
        }
    }

    @Test
    fun testGetForOptimize() = runBlocking {
        val actual = target.getForOptimize().toList()
        assertAll(
            { assertEquals(216, actual.size) },
            {
                assertEquals(
                    RsiParams(
                        timeFrame = TimeFrame.of(properties.timeFrameRange.first),
                        buyThreshold = RsiThreshold.of(properties.buyThresholdRange.first),
                        sellThreshold = RsiThreshold.of(properties.sellThresholdRange.first)
                    ),
                    actual.first()
                )
            },
            {
                assertEquals(
                    RsiParams(
                        timeFrame = TimeFrame.of(properties.timeFrameRange.last),
                        buyThreshold = RsiThreshold.of(properties.buyThresholdRange.last),
                        sellThreshold = RsiThreshold.of(properties.sellThresholdRange.last)
                    ),
                    actual.last()
                )
            }
        )
    }

    @Test
    fun testSave() = runBlocking {
        // setup:
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val params = RsiParams(
            timeFrame = TimeFrame.of(12),
            buyThreshold = RsiThreshold.of(22),
            sellThreshold = RsiThreshold.of(83)
        )

        // exercise:
        target.save(id, params)

        // verify:
        coVerify {
            strategyParamsDao.insert<RsiParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) },
                        { assertEquals(params.timeFrame.toInt(), it.timeFrame) },
                        { assertEquals(params.buyThreshold.toInt(), it.buyThreshold) },
                        { assertEquals(params.sellThreshold.toInt(), it.sellThreshold) },
                    )
                }
            )
        }
    }
}
