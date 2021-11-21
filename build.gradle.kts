import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    kotlin("plugin.jpa") version "1.6.0"
    id("com.google.cloud.tools.jib") version "3.1.4"
}

group = "com.team13"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.vladmihalcea:hibernate-types-55:2.14.0")
    implementation("net.postgis:postgis-jdbc:2.5.0")
    implementation("org.locationtech.jts:jts-core:1.16.0")
    implementation("org.hibernate:hibernate-spatial:5.4.20.Final")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val jibEnvKeys = listOf(
    "APP_NAME",
    "APP_PORT",
    "DB_URL",
    "DB_USER",
    "DB_PASSWORD",
)

tasks {
    jib {
        to {
            image = "ghcr.io/aeliseev/team-13-junction-backend"
        }
        container {
            environment = jibEnvKeys.associateWith { System.getenv(it) }
            jvmFlags = listOf("-Xmx1024m")
        }
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
