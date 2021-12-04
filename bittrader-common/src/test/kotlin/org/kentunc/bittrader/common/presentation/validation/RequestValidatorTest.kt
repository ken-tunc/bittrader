package org.kentunc.bittrader.common.presentation.validation

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.server.ServerWebInputException
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@SpringJUnitConfig(RequestValidatorTest.Config::class)
internal class RequestValidatorTest {

    private data class TestModel(@field:NotBlank val string: String, @field:Min(5) val int: Int)

    internal class Config {
        @Bean
        fun validator() = LocalValidatorFactoryBean().apply {
            afterPropertiesSet()
        }

        @Bean
        fun requestValidator() = RequestValidator(validator())
    }

    @Autowired
    private lateinit var target: RequestValidator

    @Test
    fun testValidate_valid() {
        // setup:
        val model = TestModel("test", 5)

        // exercise & verify
        assertDoesNotThrow { target.validate(model) }
    }

    @Test
    fun testValidate_invalid() {
        // setup:
        val model = TestModel("", 1)

        // exercise & verify
        assertThrows<ServerWebInputException> { target.validate(model) }
    }
}
