package edu.sakralen.task2.repository;

import edu.sakralen.task2.model.SessionCustomer;

public interface SessionCustomerRepository<K> extends Repository<SessionCustomer, K> {
    boolean deleteBySessionAndCustomerIds(Long sessionId, Long customerId);
}
