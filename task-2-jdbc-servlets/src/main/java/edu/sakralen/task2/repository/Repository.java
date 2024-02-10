package edu.sakralen.task2.repository;

import java.util.List;

public interface Repository<E, K> {
    E findById(K id);

    boolean deleteById(K id);

    List<E> findAll();

    K save(E entity);

    boolean update(E entity);
}
