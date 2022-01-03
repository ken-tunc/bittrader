package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.infrastructure.repository.BBandsParamsRepositoryImpl
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BBandsConfigurationProperties::class)
class BBandsConfiguration(private val properties: BBandsConfigurationProperties) {

    @Bean
    fun bBandsParamsRepositoryImpl(strategyParamsDao: StrategyParamsDao): BBandsParamsRepositoryImpl =
        BBandsParamsRepositoryImpl(defaultParams(), paramsForOptimize(), strategyParamsDao)

    fun defaultParams() = BBandsParams(
        timeFrame = properties.defaultTimeFrame,
        buyK = properties.defaultBuyK,
        sellK = properties.defaultSellK
    )

    fun paramsForOptimize(): List<BBandsParams> =
        properties.timeFrameRange.flatMap { timeFrame -> properties.buyKRange.map { timeFrame to it } }
            .flatMap { (timeFrame, buyK) ->
                properties.sellKRange.map { sellK ->
                    BBandsParams(
                        timeFrame = timeFrame,
                        buyK = buyK,
                        sellK = sellK
                    )
                }
            }
}
