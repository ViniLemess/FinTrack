
val kotlin_version: String by project
val logback_version: String by project
val swagger_codegen_version = "1.0.36"
val kodeinDiVersion = "7.22.0"
val mockkVersion = "1.13.12"
val kotlinxSerializationVersion = "1.7.3"
val junitVersion = "5.8.1"

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
}

group = "br.com.vinilemess.fintrack"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("org.kodein.di:kodein-di:$kodeinDiVersion")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodeinDiVersion")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swagger_codegen_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-server-tests")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}


