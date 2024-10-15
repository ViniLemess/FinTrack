
val kotlinVersion: String by project
val logbackVersion: String by project
val swaggerCodegenVersion = "1.0.36"
val kodeinDiVersion = "7.22.0"
val mockkVersion = "1.13.12"
val kotlinxSerializationVersion = "1.7.3"
val junitVersion = "5.8.1"
val mongodbKotlinVersion = "5.2.0"
val testContainersVersion = "1.20.2"

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
}

group = "br.com.vinilemess.fintrack"
version = "1.0.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Server
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-config-yaml")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Documentation
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swaggerCodegenVersion")

    // DI
    implementation("org.kodein.di:kodein-di:$kodeinDiVersion")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodeinDiVersion")

    // Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")


    // Database
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:$mongodbKotlinVersion")
    implementation("org.mongodb:bson-kotlinx:$mongodbKotlinVersion")

    // Testing
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-server-tests")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


