package edu.sakralen.task2.testutil;

import org.junit.jupiter.api.BeforeAll;

public class DatabaseTestExtension {
    private static final PostgresTestContainer POSTGRES_CONTAINER = PostgresTestContainer.getInstance();

    @BeforeAll
    static void beforeAll() {
        POSTGRES_CONTAINER.start();
        ConnectionManagerUtil.loadMockedConnectionManagerClass(
                POSTGRES_CONTAINER.getJdbcUrl(),
                POSTGRES_CONTAINER.getUsername(),
                POSTGRES_CONTAINER.getPassword()
        );
    }
}
