package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.exception.RepositorySqlException;
import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.AbstractRepository;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.repository.mapper.CustomerResultSetMapper;
import edu.sakralen.task2.repository.mapper.SessionResultSetMapper;
import edu.sakralen.task2.repository.mapper.impl.CustomerResultSetMapperImpl;
import edu.sakralen.task2.repository.mapper.impl.SessionResultSetMapperImpl;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CustomerRepositoryImpl extends AbstractRepository implements CustomerRepository<Long> {
    private static final String FIND_ALL_SQL = "SELECT id, name, surname FROM customer";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM customer WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO customer (name, surname) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE customer SET name = ?, surname = ? WHERE id = ?";
    private static final String FIND_BY_ID_WITH_SESSIONS_SQL = """
            SELECT c.id c_id, c.name c_name, c.surname c_surname,
                   s.id s_id, s.date_time s_date_time, s.price s_price
            FROM customer c
            INNER JOIN session_customer sc ON c.id = sc.customer_id
            INNER JOIN session s ON sc.session_id = s.id
            WHERE c.id = ?""";

    private static final CustomerResultSetMapper CUSTOMER_DEFAULT_MAPPER = new CustomerResultSetMapperImpl();

    public Customer findById(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return CUSTOMER_DEFAULT_MAPPER.map(resultSet);
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    public boolean deleteById(Long id) {
        try (
                Connection connection = CONNECTION_MANAGER.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    public List<Customer> findAll() {
        try (
                Connection connection = CONNECTION_MANAGER.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Customer> customers = new LinkedList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customers.add(CUSTOMER_DEFAULT_MAPPER.map(resultSet));
            }

            return customers;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Long save(Customer customer) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong("id");
                    customer.setId(id);

                    return id;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public boolean update(Customer customer) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            preparedStatement.setObject(3, customer.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Customer findByIdWithSessions(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_WITH_SESSIONS_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CustomerResultSetMapper customerMapper = new CustomerResultSetMapperImpl(
                        "c_id", "c_name", "c_surname"
                );
                SessionResultSetMapper sessionMapper = new SessionResultSetMapperImpl(
                        "s_id", "s_date_time", "s_price"
                );

                Customer customer = customerMapper.map(resultSet);
                Set<Session> sessions = new HashSet<>();

                do {
                    sessions.add(sessionMapper.map(resultSet));
                } while (resultSet.next());

                customer.setSessions(sessions);

                return customer;
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }
}
