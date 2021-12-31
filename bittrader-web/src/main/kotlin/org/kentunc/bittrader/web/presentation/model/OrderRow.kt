package org.kentunc.bittrader.web.presentation.model

import org.kentunc.bittrader.common.domain.model.order.Order
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderRow(
    val orderType: String,
    val orderSide: String,
    val price: BigDecimal?,
    val size: BigDecimal,
    val averagePrice: BigDecimal,
    val state: String,
    val orderDate: LocalDateTime
) {

    companion object {
        fun of(order: Order) = OrderRow(
            orderType = order.detail.orderType.toString(),
            orderSide = order.detail.orderSide.toString(),
            price = order.detail.price?.toBigDecimal(),
            size = order.detail.size.toBigDecimal(),
            averagePrice = order.averagePrice.toBigDecimal(),
            state = order.state.toString().lowercase(),
            orderDate = order.orderDate.toLocalDateTime()
        )
    }
}
