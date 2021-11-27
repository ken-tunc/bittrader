import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

val springmockkVersion: String by project
val archunitVersion: String by project
val okhttpVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    implementation("io.projectreactor:reactor-test")
    implementation("com.ninja-squad:springmockk:$springmockkVersion")
    implementation("com.tngtech.archunit:archunit-junit5:$archunitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")
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
