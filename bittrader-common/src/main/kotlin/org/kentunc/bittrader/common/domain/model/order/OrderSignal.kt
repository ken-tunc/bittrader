package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime

class OrderSignal private constructor(
    val detail: OrderDetail,
    val averagePrice: Price,
    val state: OrderState,
    val orderDate: DateTime
) {

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
        ): OrderSignal {
            val detail = OrderDetail.of(productCode, orderType, orderSide, price, size)
            return OrderSignal(detail, averagePrice, state, orderDate)
        }
    }
}
