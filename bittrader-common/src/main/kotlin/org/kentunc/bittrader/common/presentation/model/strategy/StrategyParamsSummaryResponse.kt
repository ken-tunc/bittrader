package org.kentunc.bittrader.common.presentation.model.strategy

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParamsSummary
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.strategy.params.BBandsParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.EmaParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.MacdParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.RsiParamsDto

data class StrategyParamsSummaryResponse(
    val productCode: ProductCode,
    val duration: Duration,
    val bBandsParams: BBandsParamsDto,
    val emaParams: EmaParamsDto,
    val macdParams: MacdParamsDto,
    val rsiParams: RsiParamsDto
) {

    companion object {
        fun of(strategyParamsSummary: StrategyParamsSummary) = StrategyParamsSummaryResponse(
            productCode = strategyParamsSummary.id.productCode,
            duration = strategyParamsSummary.id.duration,
            bBandsParams = BBandsParamsDto.of(strategyParamsSummary.bBandsParams),
            emaParams = EmaParamsDto.of(strategyParamsSummary.emaParams),
            macdParams = MacdParamsDto.of(strategyParamsSummary.macdParams),
            rsiParams = RsiParamsDto.of(strategyParamsSummary.rsiParams)
        )
    }

    fun toStrategyParamsSummary() = StrategyParamsSummary(
        id = StrategyValuesId(productCode, duration),
        bBandsParams = bBandsParams.toBBandsParams(),
        emaParams = emaParams.toEmaParams(),
        macdParams = macdParams.toMacdParams(),
        rsiParams = rsiParams.toRsiParams()
    )
}
