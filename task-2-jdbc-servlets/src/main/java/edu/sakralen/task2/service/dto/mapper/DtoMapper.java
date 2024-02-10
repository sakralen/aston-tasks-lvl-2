package edu.sakralen.task2.service.dto.mapper;

public interface DtoMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);
}
