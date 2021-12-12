val bootstrapVersion: String by project
val apexchartsVersion: String by project
dependencies {
    implementation(project(":bittrader-common"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.webjars:bootstrap:$bootstrapVersion")
    implementation("org.webjars.npm:apexcharts:$apexchartsVersion")
    testImplementation(project(":bittrader-test"))
    testImplementation("org.springframework.boot:spring-boot-starter-web")
}
