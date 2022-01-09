package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.kentunc.bittrader.candle.api.infrastructure.repository.MacdParamsRepositoryImpl
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.common.domain.model.strategy.params.MacdParams
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MacdConfigurationProperties::class)
class MacdConfiguration(private val properties: MacdConfigurationProperties) {

    @Bean
    fun macdParamsRepositoryImpl(strategyParamsDao: StrategyParamsDao): MacdParamsRepositoryImpl =
        MacdParamsRepositoryImpl(defaultParams(), paramsForOptimize(), strategyParamsDao)

    fun defaultParams() = MacdParams(
        shortTimeFrame = properties.defaultShortTimeFrame,
        longTimeFrame = properties.defaultLongTimeFrame,
        signalTimeFrame = properties.defaultSignalTimeFrame
    )

    fun paramsForOptimize(): List<MacdParams> =
        properties.shortTimeFrameRange.flatMap { shortTimeFrame -> properties.longTimeFrameRange.map { shortTimeFrame to it } }
            .filter { (shortTimeFrame, longTimeFrame) -> shortTimeFrame < longTimeFrame }
            .flatMap { (shortTimeFrame, longTimeFrame) ->
                properties.signalTimeFrameRange.map { signalTimeFrame ->
                    MacdParams(
                        shortTimeFrame = shortTimeFrame,
                        longTimeFrame = longTimeFrame,
                        signalTimeFrame = signalTimeFrame
                    )
                }
            }
}
