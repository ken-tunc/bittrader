package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.springframework.data.domain.Sort
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update

fun CandlePrimaryKey.criteria() = Criteria.where("product_code").`is`(productCode)
    .and("duration").`is`(duration)
    .and("date_time").`is`(dateTime)

fun CandleQuery.query(): Query {
    var query = Query.query(
        Criteria.where("product_code").`is`(productCode)
            .and("duration").`is`(duration)
    )
        .sort(Sort.by(Sort.Order.desc("date_time")))
    maxNum?.also { query = query.limit(it) }
    return query
}

fun CandleEntity.update() = Update.update("open", open)
    .set("close", close)
    .set("high", high)
    .set("low", low)
    .set("volume", volume)
