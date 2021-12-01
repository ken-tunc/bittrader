package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import java.math.BigDecimal

data class CommissionRateModel(@field:JsonProperty("commission_rate") val commissionRate: BigDecimal) {

    fun toModel() = CommissionRate.of(commissionRate)
}
