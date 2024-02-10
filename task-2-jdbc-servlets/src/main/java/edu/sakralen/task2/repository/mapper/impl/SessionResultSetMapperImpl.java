package edu.sakralen.task2.repository.mapper.impl;

import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.mapper.SessionResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SessionResultSetMapperImpl implements SessionResultSetMapper {
    private static final String ID_DEFAULT_COLUMN_LABEL = "id";
    private static final String ATTENDED_AT_DEFAULT_COLUMN_LABEL = "date_time";
    private static final String PRICE_DEFAULT_COLUMN_LABEL = "price";

    private final String idColumnLabel;
    private final String attendedAtColumnLabel;
    private final String priceColumnLabel;

    public SessionResultSetMapperImpl() {
        this.idColumnLabel = ID_DEFAULT_COLUMN_LABEL;
        this.attendedAtColumnLabel = ATTENDED_AT_DEFAULT_COLUMN_LABEL;
        this.priceColumnLabel = PRICE_DEFAULT_COLUMN_LABEL;
    }

    public SessionResultSetMapperImpl(String idColumnLabel, String attendedAtColumnLabel, String priceColumnLabel) {
        this.idColumnLabel = idColumnLabel;
        this.attendedAtColumnLabel = attendedAtColumnLabel;
        this.priceColumnLabel = priceColumnLabel;
    }

    @Override
    public Session map(ResultSet resultSet) throws SQLException {
        return new Session(resultSet.getLong(idColumnLabel),
                getAttendedAtAsLocalDateTime(resultSet),
                resultSet.getBigDecimal(priceColumnLabel));

    }

    private LocalDateTime getAttendedAtAsLocalDateTime(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(attendedAtColumnLabel);

        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
