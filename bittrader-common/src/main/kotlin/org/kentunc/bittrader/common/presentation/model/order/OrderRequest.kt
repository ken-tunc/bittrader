package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import javax.validation.Valid
import javax.validation.constraints.Min

data class OrderRequest(
    @field:Valid
    val detail: OrderDetailDto,
    @field:Min(0)
    val minutesToExpire: Int,
    val timeInForce: TimeInForce
) {
    companion object {
        fun of(order: Order) = OrderRequest(
            detail = OrderDetailDto.of(order.detail),
            minutesToExpire = order.minutesToExpire.toInt(),
            timeInForce = order.timeInForce
        )
    }

    fun toOrder() = Order.of(
        detail = detail.toOrderDetail(),
        minutesToExpire = MinutesToExpire.of(minutesToExpire),
        timeInForce = timeInForce
    )
}
