package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val detail: OrderDetailDto,
    val averagePrice: BigDecimal,
    val state: OrderState,
    val orderDate: LocalDateTime
) {

    companion object {
        fun of(order: Order) = OrderResponse(
            detail = OrderDetailDto.of(order.detail),
            averagePrice = order.averagePrice.toBigDecimal(),
            state = order.state,
            orderDate = order.orderDate.toLocalDateTime()
        )
    }

    fun toOrder() = Order.of(
        detail = detail.toOrderDetail(),
        averagePrice = Price.of(averagePrice),
        state = state,
        orderDate = DateTime.of(orderDate)
    )
}
