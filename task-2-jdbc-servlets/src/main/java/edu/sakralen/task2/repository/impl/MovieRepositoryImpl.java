package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.exception.RepositorySqlException;
import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.repository.AbstractRepository;
import edu.sakralen.task2.repository.MovieRepository;
import edu.sakralen.task2.repository.mapper.MovieResultSetMapper;
import edu.sakralen.task2.repository.mapper.impl.MovieResultSetMapperImpl;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MovieRepositoryImpl extends AbstractRepository implements MovieRepository<Long> {
    private static final String FIND_ALL_SQL = "SELECT id, title FROM movie";
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + " WHERE id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM movie WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO movie (title) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE movie SET title = ? WHERE id = ?";

    private static final MovieResultSetMapper DEFAULT_MOVIE_MAPPER = new MovieResultSetMapperImpl();

    public Movie findById(Long id) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return DEFAULT_MOVIE_MAPPER.map(resultSet);
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

    public List<Movie> findAll() {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Movie> movies = new LinkedList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movies.add(DEFAULT_MOVIE_MAPPER.map(resultSet));
            }

            return movies;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public Long save(Movie movie) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, movie.getTitle());

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong("id");
                    movie.setId(id);

                    return id;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }

    @Override
    public boolean update(Movie movie) {
        try (Connection connection = CONNECTION_MANAGER.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setObject(2, movie.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositorySqlException(e);
        }
    }
}
