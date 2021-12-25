package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.time.DateTime

class Order private constructor(
    val detail: OrderDetail,
    val averagePrice: Price,
    val state: OrderState,
    val orderDate: DateTime
) {

    companion object {
        fun of(detail: OrderDetail, averagePrice: Price, state: OrderState, orderDate: DateTime) =
            Order(detail, averagePrice, state, orderDate)
    }
}
