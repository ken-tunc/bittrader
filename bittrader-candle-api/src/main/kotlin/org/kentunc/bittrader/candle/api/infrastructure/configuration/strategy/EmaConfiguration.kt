package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.kentunc.bittrader.candle.api.infrastructure.repository.EmaParamsRepositoryImpl
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(EmaConfigurationProperties::class)
class EmaConfiguration(private val properties: EmaConfigurationProperties) {

    @Bean
    fun emaParamsRepositoryImpl(strategyParamsDao: StrategyParamsDao): EmaParamsRepositoryImpl =
        EmaParamsRepositoryImpl(defaultParams(), paramsForOptimize(), strategyParamsDao)

    fun defaultParams() = EmaParams(
        shortTimeFrame = TimeFrame.of(properties.defaultShortTimeFrame),
        longTimeFrame = TimeFrame.of(properties.defaultLongTimeFrame)
    )

    fun paramsForOptimize(): List<EmaParams> =
        properties.shortTimeFrameRange.flatMap { shortTimeFrame -> properties.longTimeFrameRange.map { shortTimeFrame to it } }
            .filter { (shortTimeFrame, longTimeFrame) -> shortTimeFrame < longTimeFrame }
            .map { (shortTimeFrame, longTimeFrame) ->
                EmaParams(shortTimeFrame = TimeFrame.of(shortTimeFrame), longTimeFrame = TimeFrame.of(longTimeFrame))
            }
}
