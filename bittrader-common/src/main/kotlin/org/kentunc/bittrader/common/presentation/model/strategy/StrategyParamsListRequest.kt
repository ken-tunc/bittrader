package org.kentunc.bittrader.common.presentation.model.strategy

import com.fasterxml.jackson.annotation.JsonInclude
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.presentation.model.strategy.params.BBandsParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.EmaParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.MacdParamsDto
import org.kentunc.bittrader.common.presentation.model.strategy.params.RsiParamsDto
import javax.validation.Valid

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StrategyParamsListRequest(
    val productCode: ProductCode,
    val duration: Duration,
    @field:Valid
    val bbands: BBandsParamsDto? = null,
    @field:Valid
    val ema: EmaParamsDto? = null,
    @field:Valid
    val macd: MacdParamsDto? = null,
    @field:Valid
    val rsi: RsiParamsDto? = null
) {

    fun getStrategyValuesId() = StrategyValuesId(productCode, duration)

    fun toStrategyParamsList(): List<StrategyParams> {
        return listOfNotNull(
            bbands?.toBBandsParams(),
            ema?.toEmaParams(),
            macd?.toMacdParams(),
            rsi?.toRsiParams()
        )
    }
}
