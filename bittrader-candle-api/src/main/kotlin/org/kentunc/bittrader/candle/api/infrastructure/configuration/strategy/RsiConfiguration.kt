package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.kentunc.bittrader.candle.api.infrastructure.repository.RsiParamsRepositoryImpl
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.common.domain.model.strategy.params.RsiParams
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
        timeFrame = properties.defaultTimeFrame,
        buyThreshold = properties.defaultBuyThreshold,
        sellThreshold = properties.defaultSellThreshold
    )

    fun paramsForOptimize(): List<RsiParams> =
        properties.timeFrameRange.flatMap { timeFrame -> properties.buyThresholdRange.map { timeFrame to it } }
            .flatMap { (timeFrame, buyThreshold) ->
                properties.sellThresholdRange.map { sellThreshold ->
                    RsiParams(
                        timeFrame = timeFrame,
                        buyThreshold = buyThreshold,
                        sellThreshold = sellThreshold
                    )
                }
            }
}
