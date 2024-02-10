package edu.sakralen.task2.repository;

import edu.sakralen.task2.model.Session;

import java.util.List;

public interface SessionRepository<K> extends Repository<Session, K> {
    List<Session> findAllWithMovie();

    Session findByIdWithMovie(K id);

    Session findByIdWitCustomers(K id);

    Long countCustomersOnSession(K id);
}
