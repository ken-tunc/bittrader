package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime

class OrderSignal private constructor(
    val productCode: ProductCode,
    val orderSide: OrderSide,
    val orderType: OrderType,
    val price: Price?,
    val size: Size,
    val averagePrice: Price,
    val state: OrderState,
    val orderDate: DateTime
) {

    companion object {
        fun of(
            productCode: ProductCode,
            orderSide: OrderSide,
            orderType: OrderType,
            price: Price?,
            size: Size,
            averagePrice: Price,
            state: OrderState,
            orderDate: DateTime
        ) = OrderSignal(productCode, orderSide, orderType, price, size, averagePrice, state, orderDate)
    }
}
