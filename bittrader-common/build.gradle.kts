import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
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
