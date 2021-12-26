package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import java.math.BigDecimal

data class OrderSignalRequest(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    @field:JsonProperty("child_order_type")
    val orderType: OrderType,
    val side: OrderSide,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val price: BigDecimal?,
    val size: BigDecimal,
    @field:JsonProperty("minute_to_expire")
    val minutesToExpire: Int,
    @field:JsonProperty("time_in_force")
    val timeInForce: TimeInForce
) {

    companion object {
        fun of(orderSignal: OrderSignal) = OrderSignalRequest(
            productCode = orderSignal.detail.productCode,
            orderType = orderSignal.detail.orderType,
            side = orderSignal.detail.orderSide,
            price = orderSignal.detail.price?.toBigDecimal(),
            size = orderSignal.detail.size.toBigDecimal(),
            minutesToExpire = orderSignal.minutesToExpire.toInt(),
            timeInForce = orderSignal.timeInForce
        )
    }
}
