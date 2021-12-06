import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("jacoco")
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

java.sourceCompatibility = JavaVersion.VERSION_11

allprojects {
    group = "org.kentunc"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("jacoco")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    val springmockkVersion: String by project
    val archunitVersion: String by project
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "mockito-core")
        }
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
        testImplementation("com.tngtech.archunit:archunit-junit5:$archunitVersion")
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // docker image configuration
    val dockerRegistry: String by project
    val dockerUserName: String by project
    tasks.getByName<BootBuildImage>("bootBuildImage") {
        imageName = "$dockerRegistry/$dockerUserName/${rootProject.name}/${project.name}:latest"
        environment("BP_OCI_SOURCE", "https://github.com/$dockerUserName/${rootProject.name}")
        System.getenv()["GH_ACCESS_TOKEN"]?.also {
            isPublish = true
            docker {
                publishRegistry {
                    username = dockerUserName
                    password = it
                    url = dockerRegistry
                }
            }
        }
    }

    // coverage report configuration
    tasks.test {
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    }
    tasks.jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }
    }

    tasks.withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude(
                        "**/development/**",
                        "**/test/**",
                        "**/*Application*",
                        "**/*Configuration*",
                        "**/*ConfigurationProperties*",
                        "**/*Exception",
                    )
                }
            }))
        }
    }
}

// disable tasks for root project
tasks.withType<BootJar> {
    enabled = false
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    enabled = false
}
