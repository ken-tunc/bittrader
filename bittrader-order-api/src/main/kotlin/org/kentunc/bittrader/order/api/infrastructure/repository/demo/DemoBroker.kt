package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.OrderDetail
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.OrderState
import org.kentunc.bittrader.common.domain.model.order.OrderType
import org.kentunc.bittrader.common.domain.model.quote.Size
import org.kentunc.bittrader.common.domain.model.ticker.Ticker
import org.kentunc.bittrader.common.domain.model.time.DateTime
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPublicApiClient
import org.kentunc.bittrader.common.shared.extension.log
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import java.time.LocalDateTime

class DemoBroker(
    private val db: DemoDatabase,
    private val bitflyerHttpPublicApiClient: BitflyerHttpPublicApiClient,
    private val commissionRateRepository: CommissionRateRepository
) {

    fun getBalances(): List<Balance> = db.getBalances()
        .map {
            Balance.of(
                currencyCode = it.key,
                amount = Size.of(it.value),
                available = Size.of(it.value)
            )
        }

    fun getOrderSignals(productCode: ProductCode): List<Order> = db.getOrderSignals(productCode)

    suspend fun sendOrder(orderSignal: OrderSignal) {
        val ticker = bitflyerHttpPublicApiClient.getTicker(orderSignal.detail.productCode).toTicker()
        val commissionRate = commissionRateRepository.get(orderSignal.detail.productCode)

        val currencyPair = when (orderSignal.detail.orderSide) {
            OrderSide.BUY -> buy(orderSignal, ticker, commissionRate)
            OrderSide.SELL -> sell(orderSignal, ticker, commissionRate)
            else -> throw IllegalStateException("Neutral side order is not allowed.")
        }
        val (from, into) = currencyPair

        val canExchange = db.canExchange(from, into)
        if (canExchange) {
            db.exchange(from, into)
        }

        val orderState = if (canExchange) OrderState.COMPLETED else OrderState.REJECTED
        db.addOrder(
            Order.of(
                detail = OrderDetail.of(
                    productCode = orderSignal.detail.productCode,
                    orderSide = orderSignal.detail.orderSide,
                    orderType = orderSignal.detail.orderType,
                    price = orderSignal.detail.price,
                    size = orderSignal.detail.size
                ),
                averagePrice = ticker.midPrice,
                state = orderState,
                orderDate = DateTime.of(LocalDateTime.now())
            )
        )
        log.info(
            "Send order: productCode=${orderSignal.detail.productCode}, side=${orderSignal.detail.orderSide}, " +
                "type=${orderSignal.detail.orderType}, price=${orderSignal.detail.price}, size=${orderSignal.detail.size}, " +
                "averagePrice=${ticker.midPrice}, state=$orderState"
        )
    }

    private fun buy(orderSignal: OrderSignal, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val detail = orderSignal.detail
        val actualOrderSize = when (detail.orderType) {
            OrderType.MARKET -> ticker.bestAsk.toBigDecimal() * detail.size.toBigDecimal()
            OrderType.LIMIT -> detail.price!!.toBigDecimal() * detail.size.toBigDecimal()
        }.let { Size.of(it) }
        val fee = commissionRate.fee(actualOrderSize)
        val from = Currency(detail.productCode.left, (actualOrderSize + fee).toBigDecimal())
        val into = Currency(detail.productCode.right, detail.size.toBigDecimal())

        return Pair(from, into)
    }

    private fun sell(orderSignal: OrderSignal, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val detail = orderSignal.detail
        val fee = commissionRate.fee(detail.size)
        val from = Currency(detail.productCode.right, (detail.size + fee).toBigDecimal())

        val actualOrderSize = when (detail.orderType) {
            OrderType.MARKET -> ticker.bestBid.toBigDecimal() * detail.size.toBigDecimal()
            OrderType.LIMIT -> detail.price!!.toBigDecimal() * detail.size.toBigDecimal()
        }
        val into = Currency(detail.productCode.left, actualOrderSize)

        return Pair(from, into)
    }
}
