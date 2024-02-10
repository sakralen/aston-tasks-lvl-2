package edu.sakralen.task2.service.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.dto.mapper.SessionDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {
    @Mock
    private SessionDtoMapper sessionDtoMapper;
    @Mock
    private SessionRepository<Long> sessionRepository;
    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    void givenPresentSessions_whenFoundAll_thenReturnsSessions() {
        Session session = new Session(1L, LocalDateTime.now(), new BigDecimal("2.0"));
        SessionDto sessionDto = Mappers.getMapper(SessionDtoMapper.class).toDto(session);

        List<Session> sessions = List.of(session);
        List<SessionDto> sessionDtos = List.of(sessionDto);

        Mockito.when(sessionRepository.findAllWithMovie()).thenReturn(sessions);
        Mockito.when(sessionDtoMapper.toDto(session)).thenReturn(sessionDto);

        assertEquals(sessionDtos, sessionService.findAll());
    }

    @Test
    void givenNotPresentSession_whenFound_thenReturnsNull() {
        Long sessionId = 1L;

        Mockito.when(sessionRepository.findByIdWithMovie(sessionId)).thenReturn(null);
        Mockito.when(sessionDtoMapper.toDto(null)).thenReturn(null);

        assertNull(sessionService.findById(sessionId));
    }

    @Test
    void givenPresentSession_whenFound_ThenReturnsSession() {
        Customer customer = new Customer("hello", "world");
        Long sessionId = 1L;
        Session session = new Session(sessionId, LocalDateTime.now(), new BigDecimal("1.75"));
        session.setCustomers(Set.of(customer));

        SessionDto sessionDto = Mappers.getMapper(SessionDtoMapper.class).toDto(session);

        Mockito.when(sessionRepository.findByIdWithMovie(sessionId)).thenReturn(session);
        Mockito.when(sessionDtoMapper.toDto(session)).thenReturn(sessionDto);

        assertEquals(sessionDto, sessionService.findById(sessionId));
    }

    @Test
    void givenSessionWithCustomers_whenCount_ThenReturnsCorrectCount() {
        Long sessionId = 1L;
        Long customerCount = 10L;
        Mockito.when(sessionRepository.countCustomersOnSession(sessionId)).thenReturn(10L);

        assertEquals(customerCount, sessionService.getCustomersCountAtSession(sessionId));
    }
}
