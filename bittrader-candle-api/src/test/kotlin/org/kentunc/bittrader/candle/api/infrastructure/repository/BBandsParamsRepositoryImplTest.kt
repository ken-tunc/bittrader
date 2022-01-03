package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.BBandsConfiguration
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.BBandsConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.BBandsParamsEntity
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [BBandsConfiguration::class], initializers = [ConfigDataApplicationContextInitializer::class])
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
            BBandsParams(properties.defaultTimeFrame, properties.defaultBuyK, properties.defaultSellK)
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
        val params = BBandsParams(20, 2.0, 1.0)
        coEvery { strategyParamsDao.selectLatestOne<BBandsParamsEntity>(any()) } returns BBandsParamsEntity.of(id, params)

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
                        timeFrame = properties.timeFrameRange.first,
                        buyK = properties.buyKRange.first(),
                        sellK = properties.sellKRange.first()
                    ),
                    actual.first()
                )
            },
            {
                assertEquals(
                    BBandsParams(
                        timeFrame = properties.timeFrameRange.last,
                        buyK = properties.buyKRange.last(),
                        sellK = properties.sellKRange.last()
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
        val params = BBandsParams(22, 2.2, 1.2)

        // exercise:
        target.save(id, params)

        // verify:
        coVerify {
            strategyParamsDao.insert<BBandsParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) },
                        { assertEquals(params.timeFrame, it.timeFrame) },
                        { assertEquals(params.buyK, it.buyK) },
                        { assertEquals(params.sellK, it.sellK) },
                    )
                }
            )
        }
    }
}
