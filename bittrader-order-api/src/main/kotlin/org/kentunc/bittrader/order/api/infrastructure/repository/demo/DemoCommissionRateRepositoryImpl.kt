package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import java.math.BigDecimal

class DemoCommissionRateRepositoryImpl(commissionRate: BigDecimal) : CommissionRateRepository {

    private val fixedCommissionRate = CommissionRate.of(commissionRate)

    override suspend fun get(productCode: ProductCode): CommissionRate {
        return fixedCommissionRate
    }
}
