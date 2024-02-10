package edu.sakralen.task2.util.connectionmanager.impl;

import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HikariCpConnectionManagerTest extends DatabaseTestExtension {

    @Test
    void givenMockedProperties_whenGetsConnection_thenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            try (Connection connection = HikariCpConnectionManager.getInstance().getConnection()) {
            }
        });
    }
}
