package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.time.DateTime
import org.kentunc.bittrader.common.shared.annotation.LocalDateTimeFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderSignalResponse(
    val detail: OrderDetailDto,
    val averagePrice: BigDecimal,
    val state: OrderState,
    @field:LocalDateTimeFormat
    val orderDate: LocalDateTime
) {

    companion object {
        fun of(orderSignal: OrderSignal) = OrderSignalResponse(
            detail = OrderDetailDto.of(orderSignal.detail),
            averagePrice = orderSignal.averagePrice.toBigDecimal(),
            state = orderSignal.state,
            orderDate = orderSignal.orderDate.toLocalDateTime()
        )
    }

    fun toOrderSignal() = OrderSignal.of(
        detail = detail.toOrderDetail(),
        averagePrice = Price.of(averagePrice),
        state = state,
        orderDate = DateTime.of(orderDate)
    )
}
