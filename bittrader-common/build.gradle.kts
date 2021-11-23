import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    testImplementation(project(":bittrader-test"))
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    enabled = false
}
