package org.kentunc.bittrader.common.presentation.validation

import org.springframework.validation.DataBinder
import org.springframework.validation.Validator
import org.springframework.web.server.ServerWebInputException

class RequestValidator(private val springValidator: Validator) {

    fun validate(target: Any) {
        val errors = DataBinder(target).apply {
            this.validator = springValidator
            validate()
        }.bindingResult
        if (errors.hasErrors()) {
            val message = errors.fieldErrors
                .map { "[${it.field}] ${it.defaultMessage}" }
                .toTypedArray()
                .joinToString(", ")
            throw ServerWebInputException(message)
        }
    }
}
