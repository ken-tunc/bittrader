package org.kentunc.bittrader.order.api.infrastructure.repository.live

import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Repository

@Repository
class LiveCommissionRateRepositoryImpl(private val bitflyerClient: BitflyerHttpPrivateApiClient) :
    CommissionRateRepository {

    override suspend fun get(productCode: ProductCode): CommissionRate {
        return bitflyerClient.getCommissionRate(productCode)
            .toCommissionRate()
    }
}
