package org.kentunc.bittrader.common.presentation.model.order

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderDetail
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal
import javax.validation.constraints.Min

data class OrderDetailDto(
    val productCode: ProductCode,
    val orderType: OrderType,
    val orderSide: OrderSide,
    @field:Min(0)
    val size: BigDecimal,
    @field:Min(0)
    val price: BigDecimal?
) {

    companion object {
        fun of(orderDetail: OrderDetail) = OrderDetailDto(
            productCode = orderDetail.productCode,
            orderType = orderDetail.orderType,
            orderSide = orderDetail.orderSide,
            size = orderDetail.size.toBigDecimal(),
            price = orderDetail.price?.toBigDecimal()
        )
    }

    fun toOrderDetail() = OrderDetail.of(
        productCode = productCode,
        orderType = orderType,
        orderSide = orderSide,
        size = Size.of(size),
        price = price?.let { Price.of(it) }
    )
}
