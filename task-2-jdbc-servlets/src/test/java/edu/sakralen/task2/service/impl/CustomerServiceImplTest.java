package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.CustomerRepository;
import edu.sakralen.task2.service.dto.CustomerDto;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.dto.mapper.CustomerDtoMapper;
import edu.sakralen.task2.service.dto.mapper.SessionDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerDtoMapper customerDtoMapper;
    @Mock
    private CustomerRepository<Long> customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void whenCreated_thenReturnsId() {
        Customer customer = new Customer();
        CustomerDto customerDto = Mappers.getMapper(CustomerDtoMapper.class).toDto(customer);
        Long returnedId = 1L;

        Mockito.when(customerDtoMapper.toEntity(customerDto)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(returnedId);

        assertEquals(returnedId, customerService.create(customerDto));
    }

    @Test
    void givenPresentCustomer_whenDeleted_thenReturnsTrue() {
        Long customerId = 1L;

        Mockito.when(customerRepository.deleteById(customerId)).thenReturn(true);

        assertTrue(customerService.removeById(customerId));
    }

    @Test
    void givenNotPresentCustomer_whenDeleted_thenReturnsFalse() {
        Long customerId = 1L;

        Mockito.when(customerRepository.deleteById(customerId)).thenReturn(false);

        assertFalse(customerService.removeById(customerId));
    }

    @Test
    void givenCustomerWithSessions_whenFoundSessions_thenReturnsSessions() {
        Session session = new Session();

        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setSessions(Set.of(session));

        SessionDto sessionDto = Mappers.getMapper(SessionDtoMapper.class).toDto(session);

        List<SessionDto> sessionDtos = List.of(sessionDto);

        Mockito.when(customerRepository.findByIdWithSessions(customerId)).thenReturn(customer);

        assertEquals(sessionDtos, customerService.findCustomerSessions(customerId));
    }
}
