package edu.sakralen.task2.service;

import edu.sakralen.task2.service.dto.CustomerDto;
import edu.sakralen.task2.service.dto.SessionDto;

import java.util.List;

public interface CustomerService<K> extends Service<K> {
    CustomerDto findById(K id);

    List<CustomerDto> findAll();

    K create(CustomerDto dto);

    boolean removeById(K id);

    List<SessionDto> findCustomerSessions(K customerId);

    boolean update(CustomerDto dto);
}
