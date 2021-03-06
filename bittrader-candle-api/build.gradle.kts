val ta4jVersion: String by project

dependencies {
    implementation(project(":bittrader-common"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework:spring-jdbc")
    implementation("com.h2database:h2")
    implementation("org.ta4j:ta4j-core:$ta4jVersion")
    runtimeOnly("dev.miku:r2dbc-mysql")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("mysql:mysql-connector-java")
    testImplementation(project(":bittrader-test"))
}
