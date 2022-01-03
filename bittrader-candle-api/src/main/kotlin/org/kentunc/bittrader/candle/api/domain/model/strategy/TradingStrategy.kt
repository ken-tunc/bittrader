package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.MACDIndicator
import org.ta4j.core.indicators.RSIIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.rules.ChainRule
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule
import org.ta4j.core.rules.OverIndicatorRule
import org.ta4j.core.rules.UnderIndicatorRule
import org.ta4j.core.rules.helper.ChainLink

class TradingStrategy private constructor(candleList: CandleList, macdParams: MacdParams, rsiParams: RsiParams) {

    private val barSeries: BarSeries
    private val strategy: Strategy

    companion object {
        private const val CHAIN_THRESHOLD = 15

        fun of(candleList: CandleList, macdParams: MacdParams, rsiParams: RsiParams) =
            TradingStrategy(candleList, macdParams, rsiParams)
    }

    init {
        barSeries = candleList.toBarSeries()

        // MACD indicators
        val closePriceIndicator = ClosePriceIndicator(barSeries)
        val macdIndicator = MACDIndicator(closePriceIndicator, macdParams.shortTimeFrame, macdParams.longTimeFrame)
        val emaIndicator = EMAIndicator(macdIndicator, macdParams.signalTimeFrame)

        // RSI indicators
        val rsiIndicator = RSIIndicator(closePriceIndicator, rsiParams.timeFrame)

        // rules
        val macdCrossedUpRule = CrossedUpIndicatorRule(macdIndicator, emaIndicator)
        val rsiUnderRule = UnderIndicatorRule(rsiIndicator, rsiParams.buyThreshold)
        val entryRule = ChainRule(macdCrossedUpRule, ChainLink(rsiUnderRule, CHAIN_THRESHOLD))
        val exitRule = OverIndicatorRule(rsiIndicator, rsiParams.sellThreshold)
            .or(CrossedDownIndicatorRule(macdIndicator, emaIndicator))

        strategy = BaseStrategy(entryRule, exitRule)
    }

    fun getPosition(): TradingPosition {
        val endIndex = barSeries.endIndex
        if (strategy.shouldEnter(endIndex)) {
            return TradingPosition.SHOULD_BUY
        }
        if (strategy.shouldExit(endIndex)) {
            return TradingPosition.SHOULD_SELL
        }
        return TradingPosition.NEUTRAL
    }

    fun getProfit(): Num {
        val tradingRecord = BarSeriesManager(barSeries).run(strategy)
        return GrossProfitCriterion().calculate(barSeries, tradingRecord)
    }
}
