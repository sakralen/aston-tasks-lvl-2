package edu.sakralen.task2.service;

import edu.sakralen.task2.service.dto.MovieDto;

import java.util.List;

public interface MovieService<K> extends Service<K> {
    List<MovieDto> findAll();

    MovieDto findById(K id);
}
