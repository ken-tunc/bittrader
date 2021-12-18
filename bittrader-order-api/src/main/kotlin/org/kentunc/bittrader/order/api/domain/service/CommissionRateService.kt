package org.kentunc.bittrader.order.api.domain.service

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Service

@Service
class CommissionRateService(private val commissionRateRepository: CommissionRateRepository) {

    suspend fun get(productCode: ProductCode): CommissionRate = commissionRateRepository.get(productCode)
}
