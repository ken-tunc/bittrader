package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime

class OrderSignal private constructor(
    val productCode: ProductCode,
    val orderType: OrderType,
    val orderSide: OrderSide,
    val price: Price?,
    val size: Size,
    val averagePrice: Price,
    val state: OrderState,
    val orderDate: DateTime
) {

    init {
        require(orderSide != OrderSide.NEUTRAL) { "Neutral side order is not allowed." }

        val isValidOrder = (orderType == OrderType.LIMIT && price != null) ||
                (orderType == OrderType.MARKET && price == null)
        require(isValidOrder) { "Order type and price are invalid, order=$orderType, price=$price" }
    }

    companion object {
        fun of(
            productCode: ProductCode,
            orderType: OrderType,
            orderSide: OrderSide,
            price: Price?,
            size: Size,
            averagePrice: Price,
            state: OrderState,
            orderDate: DateTime
        ) = OrderSignal(productCode, orderType, orderSide, price, size, averagePrice, state, orderDate)
    }
}
