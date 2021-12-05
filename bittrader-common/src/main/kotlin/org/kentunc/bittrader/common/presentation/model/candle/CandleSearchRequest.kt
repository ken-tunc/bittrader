package org.kentunc.bittrader.common.presentation.model.candle

import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebInputException

data class CandleSearchRequest(val productCode: ProductCode, val duration: Duration, val maxNum: Int?) {

    companion object {
        private const val PRODUCT_CODE_KEY = "product_code"
        private const val DURATION_CODE_KEY = "duration"
        private const val MAX_NUM_CODE_KEY = "max_num"

        fun from(params: MultiValueMap<String, String>): CandleSearchRequest {
            val productCode = runCatching {
                params.getFirst(PRODUCT_CODE_KEY)!!.let { ProductCode.valueOf(it) }
            }.getOrElse { throw ServerWebInputException("invalid product code.") }

            val duration = runCatching {
                params.getFirst(DURATION_CODE_KEY)!!.let { Duration.valueOf(it) }
            }.getOrElse { throw ServerWebInputException("invalid duration.") }

            val maxNum = runCatching {
                params.getFirst(MAX_NUM_CODE_KEY)?.let { Integer.valueOf(it) }
            }.getOrElse { throw ServerWebInputException("invalid max num.") }

            return CandleSearchRequest(productCode, duration, maxNum)
        }
    }

    fun toCandleQuery() = CandleQuery(productCode, duration, maxNum)

    fun toMultiValueMap(): MultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add(PRODUCT_CODE_KEY, productCode.toString())
            add(DURATION_CODE_KEY, duration.toString())
            maxNum?.also { add(MAX_NUM_CODE_KEY, it.toString()) }
        }
    }
}
