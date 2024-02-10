package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.repository.MovieRepository;
import edu.sakralen.task2.repository.impl.MovieRepositoryImpl;
import edu.sakralen.task2.service.AbstractService;
import edu.sakralen.task2.service.MovieService;
import edu.sakralen.task2.service.dto.MovieDto;
import edu.sakralen.task2.service.dto.mapper.DtoMapper;
import edu.sakralen.task2.service.dto.mapper.MovieDtoMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class MovieServiceImpl extends AbstractService<Movie, MovieDto> implements MovieService<Long> {
    private final MovieRepository<Long> movieRepository;

    public MovieServiceImpl() {
        super(Mappers.getMapper(MovieDtoMapper.class));
        this.movieRepository = new MovieRepositoryImpl();
    }

    public MovieServiceImpl(DtoMapper<Movie, MovieDto> mapper, MovieRepository<Long> movieRepository) {
        super(mapper);
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDto> findAll() {
        return movieRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public MovieDto findById(Long id) {
        if (id == null || id == 0) {
            return null;
        }

        return mapper.toDto(movieRepository.findById(id));
    }
}
