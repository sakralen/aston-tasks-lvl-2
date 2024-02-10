package edu.sakralen.task2.repository;

import edu.sakralen.task2.util.connectionmanager.ConnectionManager;
import edu.sakralen.task2.util.connectionmanager.impl.HikariCpConnectionManager;

public abstract class AbstractRepository {
    protected static final ConnectionManager CONNECTION_MANAGER = HikariCpConnectionManager.getInstance();

    protected AbstractRepository() {
    }
}
