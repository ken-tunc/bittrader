package org.kentunc.bittrader.common.domain.model.strategy

import org.ta4j.core.TradingRecord
import org.ta4j.core.num.Num

data class BackTestResult(val tradingRecord: TradingRecord, val criterionValue: Num)
