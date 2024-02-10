package edu.sakralen.task2.testutil;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {
    private static final String CREATE_TABLES_SCRIPT_NAME = "create_tables.sql";

    private static final PostgresTestContainer INSTANCE = new PostgresTestContainer()
            .withInitScript(CREATE_TABLES_SCRIPT_NAME);

    private PostgresTestContainer() {
        super(DockerImageName.parse("postgres:15-alpine"));
    }

    public static PostgresTestContainer getInstance() {
        return INSTANCE;
    }
}
