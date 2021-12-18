package org.kentunc.bittrader.order.api.application

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.order.api.domain.service.CommissionRateService
import org.springframework.stereotype.Service

@Service
class CommissionRateInteractor(private val commissionRateService: CommissionRateService) {

    suspend fun getByProductId(productCode: ProductCode): CommissionRate = commissionRateService.get(productCode)
}
