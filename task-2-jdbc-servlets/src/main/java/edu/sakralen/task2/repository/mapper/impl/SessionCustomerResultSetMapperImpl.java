package edu.sakralen.task2.repository.mapper.impl;

import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.mapper.SessionCustomerResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SessionCustomerResultSetMapperImpl implements SessionCustomerResultSetMapper {
    private static final String ID_DEFAULT_COLUMN_LABEL = "id";
    private static final String REGISTERED_AT_DEFAULT_COLUMN_LABEL = "registered_at";

    private final String idColumnLabel;
    private final String registeredAtColumnName;

    public SessionCustomerResultSetMapperImpl() {
        this.idColumnLabel = ID_DEFAULT_COLUMN_LABEL;
        this.registeredAtColumnName = REGISTERED_AT_DEFAULT_COLUMN_LABEL;
    }

    public SessionCustomerResultSetMapperImpl(String idColumnLabel, String registeredAtColumnName) {
        this.idColumnLabel = idColumnLabel;
        this.registeredAtColumnName = registeredAtColumnName;
    }

    @Override
    public SessionCustomer map(ResultSet resultSet) throws SQLException {
        return new SessionCustomer(resultSet.getLong(idColumnLabel),
                getAttendedAtAsLocalDateTime(resultSet));
    }

    private LocalDateTime getAttendedAtAsLocalDateTime(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(registeredAtColumnName);

        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
