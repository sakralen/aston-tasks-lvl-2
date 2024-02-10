package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.repository.MovieRepository;
import edu.sakralen.task2.service.dto.MovieDto;
import edu.sakralen.task2.service.dto.mapper.MovieDtoMapper;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest extends DatabaseTestExtension {
    @Mock
    private MovieDtoMapper movieDtoMapper;
    @Mock
    private MovieRepository<Long> movieRepository;
    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void givenPresentMovies_whenFoundAll_thenReturnsMovies() {
        Movie movie = new Movie(2L, "2");
        MovieDto movieDto = Mappers.getMapper(MovieDtoMapper.class).toDto(movie);

        List<Movie> movies = List.of(movie);
        List<MovieDto> movieDtos = List.of(movieDto);

        Mockito.when(movieRepository.findAll()).thenReturn(movies);
        Mockito.when(movieDtoMapper.toDto(movie)).thenReturn(movieDto);

        assertEquals(movieDtos, movieService.findAll());
    }

    @Test
    void givenPresentMovie_whenFound_thenReturnsMovie() {
        Long movieId = 2L;
        Movie movie = new Movie(movieId, "2");
        MovieDto movieDto = Mappers.getMapper(MovieDtoMapper.class).toDto(movie);

        Mockito.when(movieRepository.findById(movieId)).thenReturn(movie);
        Mockito.when(movieDtoMapper.toDto(movie)).thenReturn(movieDto);

        assertEquals(movieDto, movieService.findById(movieId));
    }

    @Test
    void givenNotPresentMovie_whenFound_thenReturnsNull() {
        Long movieId = 2L;

        Mockito.when(movieRepository.findById(movieId)).thenReturn(null);
        Mockito.when(movieDtoMapper.toDto(null)).thenReturn(null);

        assertNull(movieService.findById(movieId));
    }
}
