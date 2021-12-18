package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.*
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

    fun getOrderSignals(productCode: ProductCode): List<OrderSignal> = db.getOrderSignals(productCode)

    suspend fun sendOrder(order: Order) {
        val ticker = bitflyerHttpPublicApiClient.getTicker(order.detail.productCode).toTicker()
        val commissionRate = commissionRateRepository.get(order.detail.productCode)

        val currencyPair = when (order.detail.orderSide) {
            OrderSide.BUY -> buy(order, ticker, commissionRate)
            OrderSide.SELL -> sell(order, ticker, commissionRate)
            else -> throw IllegalStateException("Neutral side order is not allowed.")
        }
        val (from, into) = currencyPair

        val canExchange = db.canExchange(from, into)
        if (canExchange) {
            db.exchange(from, into)
        }

        val orderState = if (canExchange) OrderState.COMPLETED else OrderState.REJECTED
        db.addOrder(
            OrderSignal.of(
                detail = OrderDetail.of(
                    productCode = order.detail.productCode,
                    orderSide = order.detail.orderSide,
                    orderType = order.detail.orderType,
                    price = order.detail.price,
                    size = order.detail.size
                ),
                averagePrice = ticker.midPrice,
                state = orderState,
                orderDate = DateTime.of(LocalDateTime.now())
            )
        )
        log.info(
            "Send order: productCode=${order.detail.productCode}, side=${order.detail.orderSide}, " +
                    "type=${order.detail.orderType}, price=${order.detail.price}, size=${order.detail.size}, " +
                    "averagePrice=${ticker.midPrice}, state=$orderState"
        )
    }

    private fun buy(order: Order, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val detail = order.detail
        val actualOrderSize = when (detail.orderType) {
            OrderType.MARKET -> ticker.bestAsk.toBigDecimal() * detail.size.toBigDecimal()
            OrderType.LIMIT -> detail.price!!.toBigDecimal() * detail.size.toBigDecimal()
        }.let { Size.of(it) }
        val fee = commissionRate.fee(actualOrderSize)
        val from = Currency(detail.productCode.left, (actualOrderSize + fee).toBigDecimal())
        val into = Currency(detail.productCode.right, detail.size.toBigDecimal())

        return Pair(from, into)
    }

    private fun sell(order: Order, ticker: Ticker, commissionRate: CommissionRate): Pair<Currency, Currency> {
        val detail = order.detail
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
