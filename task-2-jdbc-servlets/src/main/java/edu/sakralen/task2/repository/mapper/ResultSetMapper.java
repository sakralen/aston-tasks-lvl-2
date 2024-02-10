package edu.sakralen.task2.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<E> {
    E map(ResultSet resultSet) throws SQLException;
}
