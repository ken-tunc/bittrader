package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.kentunc.bittrader.candle.api.infrastructure.repository.RsiParamsRepositoryImpl
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiThreshold
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RsiConfigurationProperties::class)
class RsiConfiguration(private val properties: RsiConfigurationProperties) {

    @Bean
    fun rsiParamsRepositoryImpl(strategyParamsDao: StrategyParamsDao): RsiParamsRepositoryImpl =
        RsiParamsRepositoryImpl(defaultParams(), paramsForOptimize(), strategyParamsDao)

    fun defaultParams() = RsiParams(
        timeFrame = TimeFrame.of(properties.defaultTimeFrame),
        buyThreshold = RsiThreshold.of(properties.defaultBuyThreshold),
        sellThreshold = RsiThreshold.of(properties.defaultSellThreshold)
    )

    fun paramsForOptimize(): List<RsiParams> =
        properties.timeFrameRange.flatMap { timeFrame -> properties.buyThresholdRange.map { timeFrame to it } }
            .flatMap { (timeFrame, buyThreshold) ->
                properties.sellThresholdRange.map { sellThreshold ->
                    RsiParams(
                        timeFrame = TimeFrame.of(timeFrame),
                        buyThreshold = RsiThreshold.of(buyThreshold),
                        sellThreshold = RsiThreshold.of(sellThreshold)
                    )
                }
            }
}
