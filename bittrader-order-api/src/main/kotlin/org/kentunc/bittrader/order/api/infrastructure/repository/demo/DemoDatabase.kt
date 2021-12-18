package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class DemoDatabase(initialBalances: Map<CurrencyCode, BigDecimal>) {

    private val balanceMap = ConcurrentHashMap(initialBalances)
    private val orderSignals = CopyOnWriteArrayList<OrderSignal>()

    fun getBalances() = balanceMap.toMap()

    fun canExchange(from: Currency, into: Currency): Boolean {
        return balanceMap[from.currencyCode]?.let { balance ->
            balance >= from.size && balanceMap.containsKey(into.currencyCode)
        } ?: false
    }

    fun exchange(from: Currency, into: Currency) {
        check(canExchange(from, into)) {
            "Cannot exchange currency, from=$from, into=$into"
        }
        balanceMap[from.currencyCode] = balanceMap[from.currencyCode]!!.minus(from.size)
        balanceMap[into.currencyCode] = balanceMap[into.currencyCode]!!.plus(into.size)
    }

    fun getOrderSignals(productCode: ProductCode): List<OrderSignal> =
        orderSignals.filter { it.detail.productCode == productCode }

    fun addOrder(order: OrderSignal) {
        orderSignals.add(order)
    }
}
