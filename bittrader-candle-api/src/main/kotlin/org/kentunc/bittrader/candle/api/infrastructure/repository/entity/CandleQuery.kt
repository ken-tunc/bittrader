package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Update

fun CandlePrimaryKey.criteria() = Criteria.where("product_code").`is`(productCode)
    .and("duration").`is`(duration)
    .and("date_time").`is`(dateTime)

fun CandleEntity.update() = Update.update("open", open)
    .set("close", close)
    .set("high", high)
    .set("low", low)
    .set("volume", volume)
