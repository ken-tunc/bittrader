package org.kentunc.bittrader.candle.api.infrastructure.repository.development

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.transaction.annotation.Transactional

open class EnumDao(private val template: R2dbcEntityTemplate) {

    @Transactional
    open suspend fun <E : Enum<*>> insert(value: E, name: String): Void? {
        return template.databaseClient
            .sql("insert into $name values (:value)")
            .bind("value", value.toString())
            .then()
            .awaitFirstOrNull()
    }
}
