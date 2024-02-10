package edu.sakralen.task2.service.dto.mapper;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.service.dto.MovieDto;
import org.mapstruct.Mapper;

@Mapper
public interface MovieDtoMapper extends DtoMapper<Movie, MovieDto> {
}
