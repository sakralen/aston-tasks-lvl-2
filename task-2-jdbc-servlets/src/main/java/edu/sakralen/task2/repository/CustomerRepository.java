package edu.sakralen.task2.repository;

import edu.sakralen.task2.model.Customer;

public interface CustomerRepository<K> extends Repository<Customer, K> {
    Customer findByIdWithSessions(K id);
}
