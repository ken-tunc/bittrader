package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import java.math.BigDecimal

data class CommissionRateResponse(@field:JsonProperty("commission_rate") val commissionRate: BigDecimal) {

    fun toCommissionRate() = CommissionRate.of(commissionRate)
}
