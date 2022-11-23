package br.com.armange.jpoc.spring.r2dbc.tc.postgres;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public abstract class PostgresIntegrationTest {

    public static final GenericContainer<?> POSTGRES_CONTAINER = new GenericContainer<>(DockerImageName
            .parse("postgres:12"))
            .withNetworkMode("host")
            .withEnv("POSTGRES_PASSWORD","postgres")
            .withEnv("POSTGRES_DB","POC_RX2")
            .waitingFor(Wait.forLogMessage(
                    ".+database system is ready to accept connections.+", 1));

    @BeforeAll
    public static void beforeAll() {
        POSTGRES_CONTAINER.start();
    }
}
