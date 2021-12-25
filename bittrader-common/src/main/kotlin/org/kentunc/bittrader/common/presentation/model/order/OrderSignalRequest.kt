package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import javax.validation.Valid
import javax.validation.constraints.Min

data class OrderSignalRequest(
    @field:Valid
    val detail: OrderDetailDto,
    @field:Min(0)
    val minutesToExpire: Int,
    val timeInForce: TimeInForce
) {
    companion object {
        fun of(orderSignal: OrderSignal) = OrderSignalRequest(
            detail = OrderDetailDto.of(orderSignal.detail),
            minutesToExpire = orderSignal.minutesToExpire.toInt(),
            timeInForce = orderSignal.timeInForce
        )
    }

    fun toOrderSignal() = OrderSignal.of(
        detail = detail.toOrderDetail(),
        minutesToExpire = MinutesToExpire.of(minutesToExpire),
        timeInForce = timeInForce
    )
}
