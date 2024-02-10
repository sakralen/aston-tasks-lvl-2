package edu.sakralen.task2.repository.mapper.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.repository.mapper.CustomerResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerResultSetMapperImpl implements CustomerResultSetMapper {
    private static final String ID_DEFAULT_COLUMN_LABEL = "id";
    private static final String NAME_DEFAULT_COLUMN_LABEL = "name";
    private static final String SURNAME_DEFAULT_COLUMN_LABEL = "surname";

    private final String idColumnLabel;
    private final String nameColumnLabel;
    private final String surnameColumnLabel;

    public CustomerResultSetMapperImpl() {
        this.idColumnLabel = ID_DEFAULT_COLUMN_LABEL;
        this.nameColumnLabel = NAME_DEFAULT_COLUMN_LABEL;
        this.surnameColumnLabel = SURNAME_DEFAULT_COLUMN_LABEL;
    }

    public CustomerResultSetMapperImpl(String idColumnLabel, String nameColumnLabel, String surnameColumnLabel) {
        this.idColumnLabel = idColumnLabel;
        this.nameColumnLabel = nameColumnLabel;
        this.surnameColumnLabel = surnameColumnLabel;
    }

    @Override
    public Customer map(ResultSet resultSet) throws SQLException {
        return new Customer(resultSet.getLong(idColumnLabel),
                resultSet.getString(nameColumnLabel),
                resultSet.getString(surnameColumnLabel));
    }
}
