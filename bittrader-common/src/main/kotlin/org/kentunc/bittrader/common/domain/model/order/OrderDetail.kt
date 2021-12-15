package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size

class OrderDetail private constructor(
    val productCode: ProductCode,
    val orderType: OrderType,
    val orderSide: OrderSide,
    val price: Price?,
    val size: Size
) {
    init {
        require(orderSide != OrderSide.NEUTRAL) { "Neutral side order is not allowed." }

        val isValidMarketOrder = orderType == OrderType.LIMIT && price != null
        val isValidLimitOrder = orderType == OrderType.MARKET && price == null
        require(isValidMarketOrder || isValidLimitOrder) {
            "Order type and price are invalid, order=$orderType, price=$price"
        }
    }

    companion object {
        fun of(productCode: ProductCode, orderType: OrderType, orderSide: OrderSide, price: Price?, size: Size) =
            OrderDetail(productCode, orderType, orderSide, price, size)
    }
}
