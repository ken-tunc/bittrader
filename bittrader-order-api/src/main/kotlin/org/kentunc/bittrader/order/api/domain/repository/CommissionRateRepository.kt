package org.kentunc.bittrader.order.api.domain.repository

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode

interface CommissionRateRepository {

    suspend fun get(productCode: ProductCode): CommissionRate
}
