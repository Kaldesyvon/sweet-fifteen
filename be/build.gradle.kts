import com.diffplug.gradle.spotless.SpotlessExtension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.apache.tools.ant.filters.ReplaceTokens
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("jacoco")
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.diffplug.spotless") version "6.4.1"
    id("org.sonarqube") version "3.3"
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.5.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "sk.hackkosice"

version =
    "0.0." +
        (if (System.getProperty("build.number") == null) "1"
        else System.getProperty("build.number"))

java.sourceCompatibility = JavaVersion.VERSION_11

configurations { compileOnly { extendsFrom(configurations.annotationProcessor.get()) } }

repositories { mavenCentral() }

dependencies {
    // Spring boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-jpamodelgen
    implementation("org.hibernate.orm:hibernate-jpamodelgen:6.0.2.Final")
    kapt("org.hibernate:hibernate-jpamodelgen:5.6.0.Final")

    // documentation
    implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.13")

    // json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("net.javacrumbs.shedlock:shedlock-spring:4.42.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:4.42.0")

    // double expression evaluator
    implementation("com.fathzer:javaluator:3.0.3")

    // jpa entity graph SEE COMPATIBILITY MATRIX WITH SPRING-DATA-JPA
    // https://github.com/Cosium/spring-data-jpa-entity-graph#compatibility-matrix
    // implementation("com.cosium.spring.data:spring-data-jpa-entity-graph:2.7.9")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // mapper
    implementation("org.mapstruct:mapstruct:1.5.1.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.1.Final")
    kapt("io.github.anweber:mapstruct-springlazy:1.0.0")

    // DB
    // https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8
    implementation("com.oracle.database.jdbc:ojdbc8:21.5.0.0")

    // metrics, prometheus
    // https://mvnrepository.com/artifact/io.micrometer/micrometer-registry-prometheus
    implementation("io.micrometer:micrometer-registry-prometheus:1.9.0")

    implementation("org.junit.jupiter:junit-jupiter:5.8.2")

    // https://mvnrepository.com/artifact/org.apache.tika/tika-core
    implementation("org.apache.tika:tika-core:2.4.1")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-reactor
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/fop
    implementation("org.apache.xmlgraphics:fop:2.8")

    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }

    // https://mvnrepository.com/artifact/org.testcontainers/testcontainers
    testImplementation("org.testcontainers:testcontainers:1.17.2")
    // https://mvnrepository.com/artifact/org.testcontainers/oracle-xe
    testImplementation("org.testcontainers:oracle-xe:1.17.2")
    // https://mvnrepository.com/artifact/com.icegreen/greenmail
    testImplementation("com.icegreen:greenmail:1.6.12")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.80.Final:osx-aarch_64")
}

dependencyManagement { imports { mavenBom("com.azure.spring:azure-spring-boot-bom:3.14.0") } }

configure<SpotlessExtension> {
    kotlin { ktfmt().kotlinlangStyle() }
    kotlinGradle { ktfmt().kotlinlangStyle() }
}

afterEvaluate {
    tasks.getByName("spotlessKotlinCheck").dependsOn(tasks.getByName("spotlessKotlinApply"))
    tasks
        .getByName("spotlessKotlinGradleCheck")
        .dependsOn(tasks.getByName("spotlessKotlinGradleApply"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs =
            listOf("-Xjsr305=strict", "-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "11"
    }
}

val sonarEnabled = System.getProperty("gbco-BE.sonar")?.toBoolean() ?: false
val sonarExclusions = listOf("**/metrics/**/*", "**/config/**/*")

tasks.withType<JacocoReport> {
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching { exclude(sonarExclusions) }
    )
    finalizedBy(tasks.jacocoTestCoverageVerification)

    if (sonarEnabled) {
        finalizedBy(tasks.sonarqube)
    }
}

tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            classDirectories.setFrom(
                sourceSets.main.get().output.asFileTree.matching { exclude(sonarExclusions) }
            )
            // MINIMUM COVERAGE 50% otherwise build will fail
            limit { minimum = "0.5".toBigDecimal() }
        }
    }
}

sonarqube {
    properties {
        //        property("sonar.cpd.exclusions", listOf("**/dto/**/*", "**/model/entity/**/*"))
        property("sonar.cpd.exclusions", listOf("**/model/entity/**/*"))
        property("sonar.coverage.exclusions", sonarExclusions)
        if (sonarEnabled) {
            property("sonar.projectKey", System.getProperty("gbco-BE.sonar.projectKey"))
            property("sonar.host.url", System.getProperty("gbco-BE.sonar.host.url"))
            property("sonar.login", System.getProperty("gbco-BE.sonar.login"))
        }
    }
}

tasks.withType<ProcessResources> {
    filesMatching("**/*.yaml") {
        filter(
            ReplaceTokens::class,
            "tokens" to
                mapOf(
                    "app.version" to project.version,
                    "app.buildTime" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                )
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("skipped", "failed") // "passed", "standardOut", "standardError"
        showExceptions = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showStackTraces = true
        showStandardStreams = false
    }
    jacoco { excludes += sonarExclusions }
    finalizedBy(tasks.jacocoTestReport)
}
