dependencies {
    implementation(project(":bittrader-common"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.webjars.npm:apexcharts:3.30.0")
    testImplementation(project(":bittrader-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-web")
}
