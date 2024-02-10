package edu.sakralen.task2.service;

import edu.sakralen.task2.service.dto.mapper.DtoMapper;
import edu.sakralen.task2.util.connectionmanager.ConnectionManager;
import edu.sakralen.task2.util.connectionmanager.impl.HikariCpConnectionManager;

public class AbstractService<E, D> {
    protected static final ConnectionManager CONNECTION_MANAGER = HikariCpConnectionManager.getInstance();
    protected final DtoMapper<E, D> mapper;

    public AbstractService(DtoMapper<E, D> mapper) {
        this.mapper = mapper;
    }
}
