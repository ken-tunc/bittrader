package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.pnl.ProfitLossRatioCriterion
import org.ta4j.core.cost.LinearTransactionCostModel
import org.ta4j.core.cost.ZeroCostModel
import org.ta4j.core.indicators.RSIIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.rules.OverIndicatorRule
import org.ta4j.core.rules.UnderIndicatorRule

class TradingStrategy private constructor(candleList: CandleList, rsiParams: RsiParams, bBandsParams: BBandsParams) {

    private val barSeries: BarSeries
    private val strategy: Strategy

    companion object {
        private val BACKTEST_TRANSACTION_COST = LinearTransactionCostModel(0.0015)
        private val BACKTEST_HOLDING_COST = ZeroCostModel()

        fun of(candleList: CandleList, rsiParams: RsiParams, bBandsParams: BBandsParams) =
            TradingStrategy(candleList, rsiParams, bBandsParams)
    }

    init {
        barSeries = candleList.toBarSeries()

        // RSI indicators
        val closePrice = ClosePriceIndicator(barSeries)
        val rsi = RSIIndicator(closePrice, rsiParams.timeFrame)

        // BollingerBands indicators
        val sma = SMAIndicator(closePrice, bBandsParams.timeFrame)
        val std = StandardDeviationIndicator(sma, bBandsParams.timeFrame)
        val bBandsMiddle = BollingerBandsMiddleIndicator(sma)
        val buyBBandsUpper = BollingerBandsUpperIndicator(bBandsMiddle, std, barSeries.numOf(bBandsParams.buyK))
        val sellBBandsUpper = BollingerBandsUpperIndicator(bBandsMiddle, std, barSeries.numOf(bBandsParams.sellK))

        // rules
        val entryRule = UnderIndicatorRule(rsi, rsiParams.buyThreshold)
            .and(UnderIndicatorRule(closePrice, buyBBandsUpper))
        val exitRule = OverIndicatorRule(rsi, rsiParams.sellThreshold)
            .or(OverIndicatorRule(closePrice, sellBBandsUpper))

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

    fun getCriterionValue(): Num {
        val tradingRecord = BarSeriesManager(barSeries, BACKTEST_TRANSACTION_COST, BACKTEST_HOLDING_COST).run(strategy)
        return ProfitLossRatioCriterion().calculate(barSeries, tradingRecord)
    }
}
