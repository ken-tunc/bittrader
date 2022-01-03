package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.MACDIndicator
import org.ta4j.core.indicators.SMAIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator
import org.ta4j.core.num.Num
import org.ta4j.core.rules.ChainRule
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule
import org.ta4j.core.rules.OverIndicatorRule
import org.ta4j.core.rules.helper.ChainLink

class TradingStrategy private constructor(candleList: CandleList, macdParams: MacdParams, bBandsParams: BBandsParams) {

    private val barSeries: BarSeries
    private val strategy: Strategy

    companion object {
        private const val CHAIN_THRESHOLD = 5

        fun of(candleList: CandleList, macdParams: MacdParams, bBandsParams: BBandsParams) =
            TradingStrategy(candleList, macdParams, bBandsParams)
    }

    init {
        barSeries = candleList.toBarSeries()

        // MACD indicators
        val closePrice = ClosePriceIndicator(barSeries)
        val macd = MACDIndicator(closePrice, macdParams.shortTimeFrame, macdParams.longTimeFrame)
        val ema = EMAIndicator(macd, macdParams.signalTimeFrame)

        // BollingerBands indicators
        val sma = SMAIndicator(closePrice, bBandsParams.timeFrame)
        val std = StandardDeviationIndicator(sma, bBandsParams.timeFrame)
        val bBandsMiddle = BollingerBandsMiddleIndicator(sma)
        val buyBBandsUpper = BollingerBandsUpperIndicator(bBandsMiddle, std, barSeries.numOf(bBandsParams.buyK))
        val sellBBandsUpper = BollingerBandsUpperIndicator(bBandsMiddle, std, barSeries.numOf(bBandsParams.sellK))

        // rules
        val macdCrossedUpRule = CrossedUpIndicatorRule(macd, ema)
        val bbuUpRule = OverIndicatorRule(closePrice, buyBBandsUpper)
        val entryRule = ChainRule(bbuUpRule, ChainLink(macdCrossedUpRule, CHAIN_THRESHOLD))
        val exitRule = CrossedDownIndicatorRule(closePrice, sellBBandsUpper)
            .or(CrossedDownIndicatorRule(macd, ema))

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
