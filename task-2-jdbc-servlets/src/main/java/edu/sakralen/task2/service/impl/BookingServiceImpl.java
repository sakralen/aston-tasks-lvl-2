package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.repository.SessionCustomerRepository;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.repository.impl.CustomerRepositoryImpl;
import edu.sakralen.task2.repository.impl.SessionCustomerRepositoryImpl;
import edu.sakralen.task2.repository.impl.SessionRepositoryImpl;
import edu.sakralen.task2.service.AbstractService;
import edu.sakralen.task2.service.BookingService;
import edu.sakralen.task2.service.dto.BookingDto;
import edu.sakralen.task2.service.dto.mapper.BookingDtoMapper;
import edu.sakralen.task2.service.dto.mapper.DtoMapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

public class BookingServiceImpl extends AbstractService<SessionCustomer, BookingDto> implements BookingService<Long> {
    private final SessionRepository<Long> sessionRepository;
    private final CustomerRepository<Long> customerRepository;
    private final SessionCustomerRepository<Long> sessionCustomerRepository;

    public BookingServiceImpl() {
        super(Mappers.getMapper(BookingDtoMapper.class));
        sessionRepository = new SessionRepositoryImpl();
        customerRepository = new CustomerRepositoryImpl();
        sessionCustomerRepository = new SessionCustomerRepositoryImpl();
    }

    public BookingServiceImpl(DtoMapper<SessionCustomer, BookingDto> mapper,
                              SessionRepository<Long> sessionRepository,
                              CustomerRepository<Long> customerRepository,
                              SessionCustomerRepository<Long> sessionCustomerRepository) {
        super(mapper);
        this.sessionRepository = sessionRepository;
        this.customerRepository = customerRepository;
        this.sessionCustomerRepository = sessionCustomerRepository;
    }

    @Override
    public Long bookSession(BookingDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException();
        }

        Long sessionId = dto.sessionId();
        Long customerId = dto.customerId();

        if (!isIdsValid(sessionId, customerId)) {
            return null;
        }

        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            return null;
        }

        Customer customer = customerRepository.findById(customerId);
        if (customer == null) {
            return null;
        }

        SessionCustomer sessionCustomer = mapper.toEntity(dto);
        sessionCustomer.setRegisteredAt(LocalDateTime.now());

        return sessionCustomerRepository.save(sessionCustomer);
    }

    @Override
    public boolean unbookSession(BookingDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException();
        }

        Long sessionId = dto.sessionId();
        Long customerId = dto.customerId();

        if (!isIdsValid(sessionId, customerId)) {
            return false;
        }

        return sessionCustomerRepository.deleteBySessionAndCustomerIds(sessionId, customerId);
    }

    private boolean isIdsValid(Long sessionId, Long customerId) {
        return sessionId != null && customerId != null && sessionId > 0 && customerId > 0;
    }
}
