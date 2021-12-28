package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.CommissionRateApiClient
import org.kentunc.bittrader.order.scheduler.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Repository

@Repository
class CommissionRateRepositoryImpl(private val commissionRateApiClient: CommissionRateApiClient) :
    CommissionRateRepository {

    override suspend fun get(productCode: ProductCode): CommissionRate {
        return commissionRateApiClient.get(productCode)
            .toCommissionRate()
    }
}
