package org.kentunc.bittrader.web.presentation.model

import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import java.time.LocalDateTime

data class OrderAnnotation(val orderDate: LocalDateTime, val orderSide: OrderSide) {

    companion object {
        fun of(candleList: CandleList, orderList: OrderList): List<OrderAnnotation> {
            if (candleList.isEmpty || orderList.isEmpty) {
                return listOf()
            }
            val candles = candleList.toList()
            val ordersFrom = candles.first().id.dateTime.toLocalDateTime()
            val ordersUntil = candles.last().id.dateTime.toLocalDateTime()
            return orderList.toList()
                .filter { it.orderDate.toLocalDateTime().isAfter(ordersFrom) }
                .filter { it.orderDate.toLocalDateTime().isBefore(ordersUntil) }
                .map { OrderAnnotation(it.orderDate.toLocalDateTime(), it.detail.orderSide) }
        }
    }
}
