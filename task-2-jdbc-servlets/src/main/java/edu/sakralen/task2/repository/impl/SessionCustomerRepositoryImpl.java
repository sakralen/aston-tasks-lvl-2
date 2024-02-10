package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.exception.RepositorySqlException;
import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.AbstractRepository;
import edu.sakralen.task2.repository.SessionCustomerRepository;
import edu.sakralen.task2.repository.mapper.SessionCustomerResultSetMapper;
import edu.sakralen.task2.repository.mapper.impl.SessionCustomerResultSetMapperImpl;
import edu.sakralen.task2.util.LocalDateTimeUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SessionCustomerRepositoryImpl extends AbstractRepository implements SessionCustomerRepository<Long> {
    private static final String FIND_ALL_SQL = "SELECT id, registered_at FROM session_customer";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM session_customer WHERE id = ?";
    private static final String SAVE_SQL =
            "INSERT INTO session_customer (registered_at, session_id, customer_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE session_customer SET registered_at = ?, session_id = ?, customer_id = ? WHERE id = ?";
    private static final String DELETE_BY_SESSION_AND_CUSTOMER_IDS_SQL =
            "DELETE FROM session_customer WHERE session_id = ? AND customer_id = ?";
    private static final SessionCustomerResultSetMapper MAPPER = new SessionCustomerResultSetMapperImpl();

    public SessionCustomer findById(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return MAPPER.map(resultSet);
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    public boolean deleteById(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    public List<SessionCustomer> findAll() {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<SessionCustomer> sessionCustomerList = new LinkedList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sessionCustomerList.add(MAPPER.map(resultSet));
            }

            return sessionCustomerList;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Long save(SessionCustomer sessionCustomer) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1,
                    LocalDateTimeUtils.toTimestampIfPresent(sessionCustomer.getRegisteredAt()));
            preparedStatement.setObject(2, getSessionIdIfPresent(sessionCustomer));
            preparedStatement.setObject(3, getCustomerIdIfPresent(sessionCustomer));

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong("id");
                    sessionCustomer.setId(id);

                    return id;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public boolean update(SessionCustomer sessionCustomer) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1,
                    LocalDateTimeUtils.toTimestampIfPresent(sessionCustomer.getRegisteredAt()));
            preparedStatement.setObject(2, getSessionIdIfPresent(sessionCustomer));
            preparedStatement.setObject(3, getCustomerIdIfPresent(sessionCustomer));
            preparedStatement.setObject(4, sessionCustomer.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    public boolean deleteBySessionAndCustomerIds(Long sessionId, Long customerId) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_SESSION_AND_CUSTOMER_IDS_SQL)) {
            preparedStatement.setLong(1, sessionId);
            preparedStatement.setLong(2, customerId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    private static Long getSessionIdIfPresent(SessionCustomer sessionCustomer) {
        Session session = sessionCustomer.getSession();

        return session != null ? session.getId() : null;
    }

    private static Long getCustomerIdIfPresent(SessionCustomer sessionCustomer) {
        Customer customer = sessionCustomer.getCustomer();

        return customer != null ? customer.getId() : null;
    }
}
