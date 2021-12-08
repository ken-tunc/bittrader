package org.kentunc.bittrader.candle.api.infrastructure.repository.development

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.shared.extension.log
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

open class EnumInserter(private val enumDao: EnumDao) {

    @Transactional
    open suspend fun insertEnumsIfNotPresent() {
        ProductCode.values().forEach { insertIfNotPresent(it, "product_code") }
        Duration.values().forEach { insertIfNotPresent(it, "duration") }
    }

    private suspend fun insertIfNotPresent(enumValue: Enum<*>, name: String) {
        try {
            enumDao.insert(enumValue, name)
        } catch (ex: DataIntegrityViolationException) {
            log.info("Enum value `$enumValue` already registered in database.")
        }
    }
}
