package org.kentunc.bittrader.common.test.model

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.*
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.time.DateTime
import java.time.LocalDateTime

object TestOrder {

    fun createDetail(
        productCode: ProductCode = ProductCode.BTC_JPY,
        orderType: OrderType = OrderType.LIMIT,
        orderSide: OrderSide = OrderSide.BUY,
        price: Price? = Price.of(100.0),
        size: Size = Size.of(150.0)
    ): OrderDetail {
        return OrderDetail.of(
            productCode = productCode,
            orderType = orderType,
            orderSide = orderSide,
            price = price,
            size = size
        )
    }

    fun createOrder(
        productCode: ProductCode = ProductCode.BTC_JPY,
        orderType: OrderType = OrderType.LIMIT,
        orderSide: OrderSide = OrderSide.BUY,
        price: Price? = Price.of(100.0),
        size: Size = Size.of(150.0),
        minutesToExpire: MinutesToExpire = MinutesToExpire.of(10),
        timeInForce: TimeInForce = TimeInForce.GTC
    ): OrderSignal {
        return OrderSignal.of(
            detail = createDetail(productCode, orderType, orderSide, price, size),
            minutesToExpire = minutesToExpire,
            timeInForce = timeInForce
        )
    }

    fun createOrderSignal(
        productCode: ProductCode = ProductCode.BTC_JPY,
        orderType: OrderType = OrderType.LIMIT,
        orderSide: OrderSide = OrderSide.BUY,
        price: Price? = Price.of(100.0),
        size: Size = Size.of(150.0),
        averagePrice: Price = Price.of(200.0),
        state: OrderState = OrderState.COMPLETED,
        orderDate: LocalDateTime = LocalDateTime.now()
    ): Order {
        return Order.of(
            detail = createDetail(productCode, orderType, orderSide, price, size),
            averagePrice = averagePrice,
            state = state,
            orderDate = DateTime.of(orderDate)
        )
    }
}
