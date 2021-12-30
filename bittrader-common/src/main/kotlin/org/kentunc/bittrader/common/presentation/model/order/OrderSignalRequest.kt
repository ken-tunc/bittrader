package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide

data class OrderSignalRequest(val productCode: ProductCode, val orderSide: OrderSide)
