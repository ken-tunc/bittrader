package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.quote.Price

class OrderSignalList private constructor(orderSignals: List<OrderSignal>) {

    init {
        val productCodes = orderSignals.map { it.detail.productCode }.toSet()
        require(productCodes.size <= 1) { "multiple product codes are found. got=$productCodes" }
    }

    private val sorted = orderSignals.sortedBy { it.orderDate }
    val size: Int
        get() = sorted.size
    val isEmpty: Boolean
        get() = sorted.isEmpty()

    companion object {
        fun of(orderSignals: List<OrderSignal>) = OrderSignalList(orderSignals)
    }

    fun canBuy(): Boolean {
        val lastOrder = sorted.lastOrNull()
        return when (lastOrder?.state) {
            null -> true
            OrderState.ACTIVE -> false
            OrderState.COMPLETED -> lastOrder.detail.orderSide == OrderSide.SELL
            else -> lastOrder.detail.orderSide == OrderSide.BUY
        }
    }

    fun canSell(): Boolean {
        val lastOrder = sorted.lastOrNull()
        return when (lastOrder?.state) {
            null -> false
            OrderState.ACTIVE -> false
            OrderState.COMPLETED -> lastOrder.detail.orderSide == OrderSide.BUY
            else -> lastOrder.detail.orderSide == OrderSide.SELL
        }
    }

    fun lastBuyPrice(): Price? {
        return sorted.filter { it.detail.orderSide == OrderSide.BUY }
            .lastOrNull { it.state == OrderState.COMPLETED }
            ?.detail?.price
    }

    fun toList() = sorted.toList()
}
