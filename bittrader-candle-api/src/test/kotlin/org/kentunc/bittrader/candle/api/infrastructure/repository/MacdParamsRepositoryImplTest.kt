package org.kentunc.bittrader.candle.api.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.MacdConfiguration
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.MacdConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.MacdParamsEntity
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams
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
                shortTimeFrame = TimeFrame.of(properties.defaultShortTimeFrame),
                longTimeFrame = TimeFrame.of(properties.defaultLongTimeFrame),
                signalTimeFrame = TimeFrame.of(properties.defaultSignalTimeFrame)
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
        val params = MacdParams(
            shortTimeFrame = TimeFrame.of(10),
            longTimeFrame = TimeFrame.of(25),
            signalTimeFrame = TimeFrame.of(8)
        )
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
            { assertEquals(216, actual.size) },
            {
                assertEquals(
                    MacdParams(
                        shortTimeFrame = TimeFrame.of(properties.shortTimeFrameRange.first),
                        longTimeFrame = TimeFrame.of(properties.longTimeFrameRange.first),
                        signalTimeFrame = TimeFrame.of(properties.signalTimeFrameRange.first)
                    ),
                    actual.first()
                )
            },
            {
                assertEquals(
                    MacdParams(
                        shortTimeFrame = TimeFrame.of(properties.shortTimeFrameRange.last),
                        longTimeFrame = TimeFrame.of(properties.longTimeFrameRange.last),
                        signalTimeFrame = TimeFrame.of(properties.signalTimeFrameRange.last)
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
        val params = MacdParams(
            shortTimeFrame = TimeFrame.of(12),
            longTimeFrame = TimeFrame.of(22),
            signalTimeFrame = TimeFrame.of(10)
        )

        // exercise:
        target.save(id, params)

        // verify:
        coVerify {
            strategyParamsDao.insert<MacdParamsEntity>(
                withArg {
                    assertAll(
                        { assertEquals(id.productCode, it.productCode) },
                        { assertEquals(id.duration, it.duration) },
                        { assertEquals(params.shortTimeFrame.toInt(), it.shortTimeFrame) },
                        { assertEquals(params.longTimeFrame.toInt(), it.longTimeFrame) },
                        { assertEquals(params.signalTimeFrame.toInt(), it.signalTimeFrame) },
                    )
                }
            )
        }
    }
}
