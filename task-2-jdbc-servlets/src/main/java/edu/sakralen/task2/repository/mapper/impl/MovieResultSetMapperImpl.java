package edu.sakralen.task2.repository.mapper.impl;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.repository.mapper.MovieResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieResultSetMapperImpl implements MovieResultSetMapper {
    private static final String ID_DEFAULT_COLUMN_LABEL = "id";
    private static final String TITLE_DEFAULT_COLUMN_LABEL = "title";

    private final String idColumnLabel;
    private final String titleColumnLabel;

    public MovieResultSetMapperImpl() {
        this.idColumnLabel = ID_DEFAULT_COLUMN_LABEL;
        this.titleColumnLabel = TITLE_DEFAULT_COLUMN_LABEL;
    }

    public MovieResultSetMapperImpl(String idColumnLabel, String titleColumnLabel) {
        this.idColumnLabel = idColumnLabel;
        this.titleColumnLabel = titleColumnLabel;
    }

    @Override
    public Movie map(ResultSet resultSet) throws SQLException {
        return new Movie(resultSet.getLong(idColumnLabel),
                resultSet.getString(titleColumnLabel));
    }
}
