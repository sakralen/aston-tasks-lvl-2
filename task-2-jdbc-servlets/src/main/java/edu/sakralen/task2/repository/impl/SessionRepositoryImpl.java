package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.exception.RepositorySqlException;
import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.AbstractRepository;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.repository.mapper.CustomerResultSetMapper;
import edu.sakralen.task2.repository.mapper.MovieResultSetMapper;
import edu.sakralen.task2.repository.mapper.SessionResultSetMapper;
import edu.sakralen.task2.repository.mapper.impl.CustomerResultSetMapperImpl;
import edu.sakralen.task2.repository.mapper.impl.MovieResultSetMapperImpl;
import edu.sakralen.task2.repository.mapper.impl.SessionResultSetMapperImpl;
import edu.sakralen.task2.util.LocalDateTimeUtils;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SessionRepositoryImpl extends AbstractRepository implements SessionRepository<Long> {
    private static final String FIND_ALL_SQL = "SELECT id, date_time, price FROM session";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM session WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO session (date_time, price, movie_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE session SET date_time = ?, price = ?, movie_id = ? WHERE id = ?";
    private static final String FIND_ALL_WITH_MOVIE_SQL = """
            SELECT s.id s_id, s.date_time s_date_time, s.price s_price,
                   m.id m_id, m.title m_title
            FROM session s
            INNER JOIN movie m ON s.movie_id = m.id""";
    private static final String FIND_BY_ID_WITH_MOVIE_SQL = FIND_ALL_WITH_MOVIE_SQL + " WHERE s.id = ?";
    private static final String FIND_BY_ID_WITH_MOVIE_AND_CUSTOMERS_SQL = """
            SELECT s.id s_id, s.date_time s_date_time, s.price s_price,
                   m.id m_id, m.title m_title,
                   c.id c_id, c.name c_name, c.surname c_surname
            FROM session s
            INNER JOIN movie m ON s.movie_id = m.id
            INNER JOIN session_customer sc ON s.id = sc.session_id
            INNER JOIN customer c ON sc.customer_id = c.id
            WHERE s.id = ?""";
    private static final String COUNT_CUSTOMERS_ON_SESSION_SQL =
            "SELECT COUNT(*) cnt FROM session_customer sc WHERE sc.session_id = ?";

    private static final SessionResultSetMapper SESSION_DEFAULT_MAPPER = new SessionResultSetMapperImpl();

    public Session findById(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return SESSION_DEFAULT_MAPPER.map(resultSet);
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

    public List<Session> findAll() {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Session> sessions = new LinkedList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sessions.add(SESSION_DEFAULT_MAPPER.map(resultSet));
            }

            return sessions;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Long save(Session session) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1,
                    LocalDateTimeUtils.toTimestampIfPresent(session.getAttendedAt()));
            preparedStatement.setBigDecimal(2, session.getPrice());
            preparedStatement.setObject(3, getMovieIdIfPresent(session));

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong("id");
                    session.setId(id);

                    return id;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public boolean update(Session session) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1,
                    LocalDateTimeUtils.toTimestampIfPresent(session.getAttendedAt()));
            preparedStatement.setBigDecimal(2, session.getPrice());
            preparedStatement.setObject(3, getMovieIdIfPresent(session));
            preparedStatement.setLong(4, session.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public List<Session> findAllWithMovie() {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_MOVIE_SQL)) {
            List<Session> sessions = new LinkedList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sessions.add(getSessionWithMovie(resultSet));
            }

            return sessions;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Session findByIdWithMovie(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_WITH_MOVIE_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getSessionWithMovie(resultSet);
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Session findByIdWitCustomers(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_WITH_MOVIE_AND_CUSTOMERS_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Session session = getSessionWithMovie(resultSet);

                CustomerResultSetMapper customerMapper = new CustomerResultSetMapperImpl(
                        "c_id", "c_name", "c_surname"
                );

                Set<Customer> customers = new HashSet<>();
                do {
                    customers.add(customerMapper.map(resultSet));
                } while (resultSet.next());

                session.setCustomers(customers);

                return session;
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Long countCustomersOnSession(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_CUSTOMERS_ON_SESSION_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("cnt");
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    private static Long getMovieIdIfPresent(Session session) {
        Movie movie = session.getMovie();

        return movie != null ? movie.getId() : null;
    }

    private static Session getSessionWithMovie(ResultSet resultSet) throws SQLException {
        SessionResultSetMapper sessionMapper = new SessionResultSetMapperImpl(
                "s_id", "s_date_time", "s_price"
        );
        MovieResultSetMapper movieMapper = new MovieResultSetMapperImpl(
                "m_id", "m_title"
        );

        Session session = sessionMapper.map(resultSet);
        Movie movie = movieMapper.map(resultSet);
        session.setMovie(movie);

        return session;
    }
}
