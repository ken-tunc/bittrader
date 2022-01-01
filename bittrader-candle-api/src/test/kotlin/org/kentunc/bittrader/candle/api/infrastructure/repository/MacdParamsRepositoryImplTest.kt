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
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.MacdConfiguration
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.MacdConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.MacdParamsEntity
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(
    classes = [MacdConfiguration::class],
    initializers = [ConfigDataApplicationContextInitializer::class]
)
internal class MacdParamsRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var strategyParamsDao: StrategyParamsDao

    @Autowired
    private lateinit var properties: MacdConfigurationProperties

    @Autowired
    private lateinit var target: MacdParamsRepositoryImpl

    @Test
    fun testGet_default() = runBlocking {
        // setup:
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        coEvery { strategyParamsDao.selectLatestOne<MacdParamsEntity>(any()) } returns null
        val expected = StrategyValues.of(
            id,
            MacdParams(
                properties.defaultShortTimeFrame,
                properties.defaultLongTimeFrame,
                properties.defaultSignalTimeFrame
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
            strategyParamsDao.selectLatestOne<MacdParamsEntity>(
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
        val params = MacdParams(10, 25, 8)
        coEvery { strategyParamsDao.selectLatestOne<MacdParamsEntity>(any()) } returns MacdParamsEntity.of(id, params)

        // exercise:
        val actual = target.get(id)

        // verify:
        assertAll(
            { assertEquals(id, actual.id) },
            { assertEquals(params, actual.params) }
        )
        coVerify {
            strategyParamsDao.selectLatestOne<MacdParamsEntity>(
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
            { assertEquals(1320, actual.size) },
            {
                assertEquals(
                    MacdParams(
                        shortTimeFrame = properties.shortTimeFrameRange.first,
                        longTimeFrame = properties.longTimeFrameRange.first,
                        signalTimeFrame = properties.signalTimeFrameRange.first
                    ),
                    actual.first()
                )
            },
            {
                assertEquals(
                    MacdParams(
                        shortTimeFrame = properties.shortTimeFrameRange.last,
                        longTimeFrame = properties.longTimeFrameRange.last,
                        signalTimeFrame = properties.signalTimeFrameRange.last
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
        val params = MacdParams(12, 22, 10)

        // exercise:
        target.save(id, params)

        // verify:
        coVerify {
            strategyParamsDao.insert<MacdParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) },
                        { assertEquals(params.shortTimeFrame, it.shortTimeFrame) },
                        { assertEquals(params.longTimeFrame, it.longTimeFrame) },
                        { assertEquals(params.signalTimeFrame, it.signalTimeFrame) },
                    )
                }
            )
        }
    }
}
