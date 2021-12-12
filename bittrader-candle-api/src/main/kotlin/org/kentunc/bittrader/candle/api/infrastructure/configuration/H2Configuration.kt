package org.kentunc.bittrader.candle.api.infrastructure.configuration

import io.r2dbc.spi.ConnectionFactory
import org.h2.tools.Server
import org.kentunc.bittrader.candle.api.infrastructure.development.H2ConsoleServer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Profile("h2")
@Configuration
class H2Configuration {

    @Bean
    fun initializer(
        @Qualifier("connectionFactory") connectionFactory: ConnectionFactory,
        @Value("classpath:schema-h2.sql") schemaResource: Resource,
        @Value("classpath:data-h2.sql") dataResource: Resource
    ): ConnectionFactoryInitializer {
        return ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            val schemaPopulator = ResourceDatabasePopulator(schemaResource)
            val dataPopulator = ResourceDatabasePopulator(dataResource)
            setDatabasePopulator(CompositeDatabasePopulator(schemaPopulator, dataPopulator))
        }
    }

    @Bean
    @Profile("local")
    fun server(@Value("\${bittrader.h2.console.port}") port: String): Server =
        Server.createWebServer("-webPort", port, "-tcpAllowOthers")

    @Bean
    @Profile("local")
    fun h2ConsoleServer(server: Server) = H2ConsoleServer(server)
}
