package edu.sakralen.task2.servlet;

import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.service.BookingService;
import edu.sakralen.task2.service.SessionService;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.dto.mapper.SessionDtoMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SessionServletTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private BookingService<Long> bookingService;
    @Mock
    private SessionService<Long> sessionService;
    @InjectMocks
    private SessionServlet sessionServlet;

    @Test
    void givenEmptyPathAndPresentSessions_whenDoGet_thenWritesSessions() throws ServletException, IOException {
        Session session = new Session();
        SessionDto sessionDto = Mappers.getMapper(SessionDtoMapper.class).toDto(session);
        List<SessionDto> sessionDtos = List.of(sessionDto);

        Mockito.when(req.getPathInfo()).thenReturn("/");
        Mockito.when(sessionService.findAll()).thenReturn(sessionDtos);
        Mockito.when(resp.getWriter()).thenReturn(printWriter);

        sessionServlet.doGet(req, resp);

        Mockito.verify(resp).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(resp).setContentType("text/plain");
        Mockito.verify(printWriter).write(sessionDtos.toString());
    }

    @Test
    void givenIncorrectPathInfo_whenDoGet_ThenSendsBadRequest() throws ServletException, IOException {
        Mockito.when(req.getPathInfo()).thenReturn("/something");

        sessionServlet.doGet(req, resp);

        Mockito.verify(resp).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void givenCorrectUrlAndExistingSession_whenDoGet_ThenWritesSession() throws ServletException, IOException {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        SessionDto sessionDto = Mappers.getMapper(SessionDtoMapper.class).toDto(session);

        Mockito.when(req.getPathInfo()).thenReturn("/1");
        Mockito.when(sessionService.findById(sessionId)).thenReturn(sessionDto);
        Mockito.when(resp.getWriter()).thenReturn(printWriter);

        sessionServlet.doGet(req, resp);

        Mockito.verify(resp).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(resp).setContentType("text/plain");
        Mockito.verify(printWriter).write(sessionDto.toString());
    }

    @Test
    void givenUrlOfNotExistingSession_thenSendsNotFound() throws ServletException, IOException {
        Long movieId = 1L;

        Mockito.when(req.getPathInfo()).thenReturn("/1");
        Mockito.when(sessionService.findById(movieId)).thenReturn(null);

        sessionServlet.doGet(req, resp);

        Mockito.verify(resp).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void givenUrlWithCustomers_thenSendCustomerCount() throws ServletException, IOException {
        Long sessionId = 1L;
        Long customersCount = 10L;

        Mockito.when(req.getPathInfo()).thenReturn("/1/customers/");
        Mockito.when(sessionService.getCustomersCountAtSession(sessionId)).thenReturn(customersCount);
        Mockito.when(resp.getWriter()).thenReturn(printWriter);

        sessionServlet.doGet(req, resp);

        Mockito.verify(resp).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(resp).setContentType("text/plain");
        Mockito.verify(printWriter).write(customersCount.toString());
    }

    @Test
    void givenUrlWithCustomersId_whenDoPost_thenWritesBookingId() {

    }

    @Test
    void givenUrlWithIncorrectCustomersId_whenDoPost_thenSendsNotFound() {

    }

    @Test
    void givenUrlWithCustomerId_whenDoDelete_thenWritesSuccessful() {

    }

    @Test
    void givenUrlWithIncorrectCustomerId_whenDoDelete_thenSendNotFound() {

    }
}
