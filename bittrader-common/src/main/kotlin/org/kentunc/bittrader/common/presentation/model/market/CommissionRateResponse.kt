package org.kentunc.bittrader.common.presentation.model.market

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import java.math.BigDecimal

data class CommissionRateResponse(val commissionRate: BigDecimal) {

    companion object {
        fun of(commissionRate: CommissionRate) = CommissionRateResponse(commissionRate.toBigDecimal())
    }

    fun toCommissionRate() = CommissionRate.of(commissionRate)
}
