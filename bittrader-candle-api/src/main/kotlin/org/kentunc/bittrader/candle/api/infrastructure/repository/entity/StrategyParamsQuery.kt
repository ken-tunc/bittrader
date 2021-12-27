package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.springframework.data.domain.Sort
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query

fun StrategyParamsPrimaryKey.latest() =
    Query.query(
        Criteria.where("product_code").`is`(productCode)
            .and("duration").`is`(duration)
    )
        .sort(Sort.by(Sort.Order.desc("created_at")))
