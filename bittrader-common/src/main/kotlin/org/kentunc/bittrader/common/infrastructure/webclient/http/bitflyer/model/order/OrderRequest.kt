package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import java.math.BigDecimal

data class OrderRequest(
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
        fun of(order: Order) = OrderRequest(
            productCode = order.productCode,
            orderType = order.orderType,
            side = order.orderSide,
            price = order.price?.toBigDecimal(),
            size = order.size.toBigDecimal(),
            minutesToExpire = order.minutesToExpire.toInt(),
            timeInForce = order.timeInForce
        )
    }
}
