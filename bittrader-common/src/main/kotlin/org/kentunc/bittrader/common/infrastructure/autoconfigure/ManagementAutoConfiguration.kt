package org.kentunc.bittrader.common.infrastructure.autoconfigure

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:autoconfigure/application-management.yaml")
@ConditionalOnProperty(prefix = "bittrader.management", name = ["enabled"], havingValue = "true")
class ManagementAutoConfiguration
