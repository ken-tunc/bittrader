package org.kentunc.bittrader.candle.api.infrastructure.configuration

import io.r2dbc.spi.ConnectionFactory
import org.h2.tools.Server
import org.kentunc.bittrader.candle.api.infrastructure.repository.development.EnumDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.development.EnumInserter
import org.kentunc.bittrader.candle.api.infrastructure.repository.development.H2DatabaseInitializer
import org.kentunc.bittrader.candle.api.infrastructure.development.H2ConsoleServer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Profile("h2")
@Configuration
class H2Configuration {

    @Bean
    fun initializer(
        @Qualifier("connectionFactory") connectionFactory: ConnectionFactory,
        @Value("classpath:schema-h2.sql") resource: Resource
    ): ConnectionFactoryInitializer {
        return ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(ResourceDatabasePopulator(resource))
        }
    }

    @Bean
    fun enumDao(template: R2dbcEntityTemplate) = EnumDao(template)

    @Bean
    fun enumInserter(enumDao: EnumDao) = EnumInserter(enumDao)

    @Bean
    fun databaseInitializer(enumInserter: EnumInserter) = H2DatabaseInitializer(enumInserter)

    @Bean
    @Profile("local")
    fun server(@Value("\${bittrader.h2.console.port}") port: String): Server =
        Server.createWebServer("-webPort", port, "-tcpAllowOthers")

    @Bean
    @Profile("local")
    fun h2ConsoleServer(server: Server) = H2ConsoleServer(server)
}
