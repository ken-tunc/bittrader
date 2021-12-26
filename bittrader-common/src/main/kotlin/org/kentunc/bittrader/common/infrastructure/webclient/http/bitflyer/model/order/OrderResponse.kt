package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order

import com.fasterxml.jackson.annotation.JsonProperty
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderDetail
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    @field:JsonProperty("product_code")
    val productCode: ProductCode,
    val side: OrderSide,
    @field:JsonProperty("child_order_type")
    val orderType: OrderType,
    val price: BigDecimal,
    val size: BigDecimal,
    @field:JsonProperty("average_price")
    val averagePrice: BigDecimal,
    @field:JsonProperty("child_order_state")
    val state: OrderState,
    @field:JsonProperty("child_order_date")
    val orderDate: LocalDateTime
) {
    companion object {
        private val MARKET_PRICE_HOLDER = BigDecimal.ZERO
    }

    fun toOrder() = Order.of(
        detail = OrderDetail.of(
            productCode = productCode,
            orderSide = side,
            orderType = orderType,
            price = if (price.compareTo(MARKET_PRICE_HOLDER) == 0) null else Price.of(price),
            size = Size.of(size)
        ),
        averagePrice = Price.of(averagePrice),
        state = state,
        orderDate = DateTime.of(orderDate)
    )
}
