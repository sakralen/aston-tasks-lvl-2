package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.repository.SessionCustomerRepository;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.service.dto.BookingDto;
import edu.sakralen.task2.service.dto.mapper.BookingDtoMapper;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest extends DatabaseTestExtension {
    @Mock
    private BookingDtoMapper dtoMapper;
    @Mock
    private SessionRepository<Long> sessionRepository;
    @Mock
    private CustomerRepository<Long> customerRepository;
    @Mock
    private SessionCustomerRepository<Long> sessionCustomerRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void givenCorrectDto_whenBooked_thenReturnsNotNullId() {
        Long sessionId = 1L;
        Long customerId = 1L;

        BookingDto dto = new BookingDto(sessionId, customerId);

        Session session = new Session();
        session.setId(sessionId);

        Customer customer = new Customer();
        customer.setId(customerId);

        SessionCustomer sessionCustomer = new SessionCustomer();
        sessionCustomer.setSession(session);
        sessionCustomer.setCustomer(customer);

        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(session);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(customer);
        Mockito.when(dtoMapper.toEntity(dto)).thenReturn(sessionCustomer);
        Mockito.when(sessionCustomerRepository.save(sessionCustomer)).thenReturn(1L);

        assertNotNull(bookingService.bookSession(dto));
    }

    @Test
    void givenNotPresentCustomer_whenBooked_thenReturnedNull() {
        Long sessionId = 1L;
        Long customerId = 1L;

        BookingDto dto = new BookingDto(sessionId, customerId);

        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(null);

        assertNull(bookingService.bookSession(dto));
    }

    @Test
    void givenNotPresentSession_whenBooked_thenReturnedNull() {
        Long sessionId = 1L;
        Long customerId = 1L;

        BookingDto dto = new BookingDto(sessionId, customerId);

        Session session = new Session();
        session.setId(sessionId);

        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(session);
        Mockito.when(customerRepository.findById(customerId)).thenReturn(null);

        assertNull(bookingService.bookSession(dto));
    }

    @Test
    void givenCorrectDto_whenUnbooked_thenReturnsTrue() {
        Long sessionId = 1L;
        Long customerId = 1L;

        BookingDto dto = new BookingDto(sessionId, customerId);

        Mockito.when(sessionCustomerRepository.deleteBySessionAndCustomerIds(sessionId, customerId)).thenReturn(true);

        assertTrue(bookingService.unbookSession(dto));
    }

    @Test
    void givenNotPresentSession_whenUnbooked_thenReturnsFalse() {
        Long sessionId = 1L;
        Long customerId = 1L;

        BookingDto dto = new BookingDto(sessionId, customerId);

        Mockito.when(sessionCustomerRepository.deleteBySessionAndCustomerIds(sessionId, customerId)).thenReturn(false);

        assertFalse(bookingService.unbookSession(dto));
    }
}
