import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

val ta4jVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // TODO: change to `implementation`
    api("org.ta4j:ta4j-core:$ta4jVersion")
    testImplementation(project(":bittrader-test"))
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.withType<BootBuildImage> {
    enabled = false
}
