package org.kentunc.bittrader.web.domain.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList

interface OrderRepository {

    suspend fun find(productCode: ProductCode): OrderList
}
