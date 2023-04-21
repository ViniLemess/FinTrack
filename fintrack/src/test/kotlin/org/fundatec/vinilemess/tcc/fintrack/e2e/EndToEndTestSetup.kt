package org.fundatec.vinilemess.tcc.fintrack.e2e

import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2ETestSetup {
    val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest"))

    @BeforeAll
    fun setup() {
        mongoDBContainer.start()
    }
}