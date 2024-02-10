package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.repository.impl.CustomerRepositoryImpl;
import edu.sakralen.task2.service.AbstractService;
import edu.sakralen.task2.service.CustomerService;
import edu.sakralen.task2.service.dto.CustomerDto;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.dto.mapper.CustomerDtoMapper;
import edu.sakralen.task2.service.dto.mapper.DtoMapper;
import edu.sakralen.task2.service.dto.mapper.SessionDtoMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class CustomerServiceImpl extends AbstractService<Customer, CustomerDto> implements CustomerService<Long> {
    private final CustomerRepository<Long> customerRepository;

    public CustomerServiceImpl(DtoMapper<Customer, CustomerDto> mapper, CustomerRepository<Long> customerRepository) {
        super(mapper);
        this.customerRepository = customerRepository;
    }

    public CustomerServiceImpl() {
        super(Mappers.getMapper(CustomerDtoMapper.class));
        this.customerRepository = new CustomerRepositoryImpl();
    }

    @Override
    public CustomerDto findById(Long id) {
        return mapper.toDto(customerRepository.findByIdWithSessions(id));
    }

    @Override
    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public Long create(CustomerDto dto) {
        if (dto == null || dto.id() != null) {
            throw new IllegalArgumentException();
        }

        return customerRepository.save(mapper.toEntity(dto));
    }

    @Override
    public boolean removeById(Long id) {
        if (id == null || id == 0) {
            throw new IllegalArgumentException();
        }

        return customerRepository.deleteById(id);
    }

    @Override
    public List<SessionDto> findCustomerSessions(Long customerId) {
        if (customerId == null || customerId == 0) {
            throw new IllegalArgumentException();
        }

        Customer customer = customerRepository.findByIdWithSessions(customerId);
        if (customer == null) {
            return null;
        }

        return customer.getSessions().stream()
                .map(Mappers.getMapper(SessionDtoMapper.class)::toDto)
                .toList();
    }

    @Override
    public boolean update(CustomerDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException();
        }

        return customerRepository.update(mapper.toEntity(dto));
    }
}
