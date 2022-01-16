package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.BBandsConfiguration
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.BBandsConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.BBandsParamsEntity
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsK
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(
    classes = [BBandsConfiguration::class],
    initializers = [ConfigDataApplicationContextInitializer::class]
)
internal class BBandsParamsRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var strategyParamsDao: StrategyParamsDao

    @Autowired
    private lateinit var properties: BBandsConfigurationProperties

    @Autowired
    private lateinit var target: BBandsParamsRepositoryImpl

    @Test
    fun testGet_default() = runBlocking {
        // setup:
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        coEvery { strategyParamsDao.selectLatestOne<BBandsParamsEntity>(any()) } returns null
        val expected = StrategyValues.of(
            id,
            BBandsParams(
                timeFrame = TimeFrame.of(properties.defaultTimeFrame),
                buyK = BBandsK.of(properties.defaultBuyK),
                sellK = BBandsK.of(properties.defaultSellK)
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
            strategyParamsDao.selectLatestOne<BBandsParamsEntity>(
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
        val params = BBandsParams(
            timeFrame = TimeFrame.of(20),
            buyK = BBandsK.of(2.0),
            sellK = BBandsK.of(1.0)
        )
        coEvery { strategyParamsDao.selectLatestOne<BBandsParamsEntity>(any()) } returns BBandsParamsEntity.of(
            id,
            params
        )

        // exercise:
        val actual = target.get(id)

        // verify:
        assertAll(
            { assertEquals(id, actual.id) },
            { assertEquals(params, actual.params) }
        )
        coVerify {
            strategyParamsDao.selectLatestOne<BBandsParamsEntity>(
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
            { assertEquals(100, actual.size) },
            {
                assertEquals(
                    BBandsParams(
                        timeFrame = TimeFrame.of(properties.timeFrameRange.first),
                        buyK = BBandsK.of(properties.buyKRange.first()),
                        sellK = BBandsK.of(properties.sellKRange.first())
                    ),
                    actual.first()
                )
            },
            {
                assertEquals(
                    BBandsParams(
                        timeFrame = TimeFrame.of(properties.timeFrameRange.last),
                        buyK = BBandsK.of(properties.buyKRange.last()),
                        sellK = BBandsK.of(properties.sellKRange.last())
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
        val params = BBandsParams(TimeFrame.of(22), BBandsK.of(2.2), BBandsK.of(1.2))

        // exercise:
        target.save(id, params)

        // verify:
        coVerify {
            strategyParamsDao.insert<BBandsParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) },
                        { assertEquals(params.timeFrame.toInt(), it.timeFrame) },
                        { assertEquals(params.buyK.toDouble(), it.buyK) },
                        { assertEquals(params.sellK.toDouble(), it.sellK) },
                    )
                }
            )
        }
    }
}
