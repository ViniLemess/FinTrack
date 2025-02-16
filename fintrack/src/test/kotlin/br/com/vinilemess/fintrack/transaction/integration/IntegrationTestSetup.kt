package br.com.vinilemess.fintrack.transaction.integration

import br.com.vinilemess.fintrack.transaction.Transactions
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class IntegrationTestSetup {

    @OptIn(ExperimentalSerializationApi::class)
    protected val deserializer = Json(Json.Default) {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        explicitNulls = false
    }

    protected companion object {
        private val postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:latest")).apply {
            withDatabaseName("fintrack_db")
            withPassword("test123")
            setPortBindings(listOf("9999:5432"))
        }

        init {
            postgresContainer.start()
        }
    }

    fun setupApplicationTest(test: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            environment {
                val applicationConfig = ApplicationConfig("application-test.yaml")
                config = applicationConfig.mergeWith(
                    MapApplicationConfig(
                        "postgres.database" to postgresContainer.databaseName,
                        "postgres.host" to postgresContainer.host,
                        "postgres.port" to "9999",
                        "postgres.username" to postgresContainer.username,
                        "postgres.password" to "test123",
                    )
                )
            }
            startApplication()
            test()
        }
    }

    @AfterEach
    fun cleanupDatabase() {
        transaction {
            SchemaUtils.drop(Transactions)
            SchemaUtils.create(Transactions)
        }
    }
}