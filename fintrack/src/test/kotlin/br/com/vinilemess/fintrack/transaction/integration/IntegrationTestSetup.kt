package br.com.vinilemess.fintrack.transaction.integration

import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class IntegrationTestSetup {

    protected val deserializer = Json(Json.Default) {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    companion object {
        private val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))

        init {
            mongoDBContainer.start()
        }

        fun getMongoConnectionString(): String {
            return mongoDBContainer.replicaSetUrl
        }
    }

    fun integrationTestApplication(test: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            environment {
                val applicationConfig = ApplicationConfig("application-test.yaml")
                config = applicationConfig.mergeWith(
                    MapApplicationConfig(
                        "mongodb.host" to "localhost",
                        "mongodb.admin" to "true",
                        "mongodb.username" to "testUser",
                        "mongodb.password" to "testPass",
                        "mongodb.database" to "test",
                        "mongodb.connection-string" to getMongoConnectionString()
                    )
                )
            }
            test()
        }
    }
}