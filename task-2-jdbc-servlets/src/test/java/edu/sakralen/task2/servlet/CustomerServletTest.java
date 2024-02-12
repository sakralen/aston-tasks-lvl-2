package edu.sakralen.task2.servlet;

import edu.sakralen.task2.service.BookingService;
import edu.sakralen.task2.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServletTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private BookingService<Long> bookingService;
    @Mock
    private CustomerService<Long> customerService;
    @InjectMocks
    private SessionServlet sessionServlets;

    @Test
    void test() {

    }
}
