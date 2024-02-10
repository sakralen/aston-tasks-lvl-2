package edu.sakralen.task2.service;

import edu.sakralen.task2.service.dto.SessionDto;

import java.util.List;

public interface SessionService<K> extends Service<K> {
    List<SessionDto> findAll();

    SessionDto findById(K id);

    Long getCustomersCountAtSession(K id);
}
