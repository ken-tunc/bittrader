package org.kentunc.bittrader.candle.api.domain.model.strategy

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.MacdConfigurationProperties
import org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy.RsiConfigurationProperties
import org.kentunc.bittrader.candle.api.test.CandleApiTest
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.shared.extension.log
import org.springframework.beans.factory.annotation.Autowired
import ta4jexamples.loaders.CsvBarsLoader

@CandleApiTest
// @TestPropertySource(
//     properties = ["logging.level.org.ta4j.core=TRACE"]
// )
internal class TradingStrategyPerformanceTest {

    @Autowired
    private lateinit var macdProperties: MacdConfigurationProperties
    @Autowired
    private lateinit var rsiProperties: RsiConfigurationProperties

    @Test
    fun measurePerformance() {
        // setup:
        val barSeries = CsvBarsLoader.loadCsvSeries("data/btc_jpy_dummy.csv")
        val candleList = mockk<CandleList>()
        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries

        val macdParams = MacdParams(
            shortTimeFrame = macdProperties.defaultShortTimeFrame,
            longTimeFrame = macdProperties.defaultLongTimeFrame,
            signalTimeFrame = macdProperties.defaultSignalTimeFrame
        )
        val rsiParams = RsiParams(
            timeFrame = rsiProperties.defaultTimeFrame,
            buyThreshold = rsiProperties.defaultBuyThreshold,
            sellThreshold = rsiProperties.defaultSellThreshold
        )

        // exercise:
        val profit = TradingStrategy.of(candleList, macdParams, rsiParams).getProfit()

        // verify:
        log.info("strategy profit: $profit")
        assertTrue(profit.isPositive)
    }
}
