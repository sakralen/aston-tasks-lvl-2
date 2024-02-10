package edu.sakralen.task2.servlet;

import edu.sakralen.task2.service.BookingService;
import edu.sakralen.task2.service.CustomerService;
import edu.sakralen.task2.service.dto.BookingDto;
import edu.sakralen.task2.service.dto.CustomerDto;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.impl.BookingServiceImpl;
import edu.sakralen.task2.service.impl.CustomerServiceImpl;
import edu.sakralen.task2.util.JsonUtils;
import edu.sakralen.task2.util.PathInfoUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private final CustomerService<Long> customerService;
    private final BookingService<Long> bookingService;

    public CustomerServlet() {
        this.customerService = new CustomerServiceImpl();
        this.bookingService = new BookingServiceImpl();
    }

    public CustomerServlet(CustomerService<Long> customerService, BookingService<Long> bookingService) {
        this.customerService = customerService;
        this.bookingService = bookingService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            doGetFindAll(resp);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (!doGetValidateUrlTokens(urlTokens)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (urlTokens.length == 1) {
            doGetFindById(urlTokens, resp);
            return;
        }

        if (urlTokens.length == 2) {
            doGetFindCustomerSessions(urlTokens, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            doPostCreateCustomer(req, resp);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (!doPostValidateUrlTokensForBooking(urlTokens)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        doPostBookSession(urlTokens, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (urlTokens.length > 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long customerId = PathInfoUtils.parseId(urlTokens[0]);
        if (customerId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Map<String, Object> customerParams = JsonUtils.toMap(req.getReader());
        if (doPostValidateCustomerParams(customerParams)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String name = (String) customerParams.get("name");
        String surname = (String) customerParams.get("surname");

        CustomerDto customerDto = new CustomerDto(null, name, surname, null);

        if (customerService.update(customerDto)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write("Successful");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (!doDeleteValidateUrlTokens(urlTokens)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (urlTokens.length == 1) {
            doDeleteRemoveCustomer(urlTokens, resp);
            return;
        }

        doDeleteUnbookSession(urlTokens, resp);
    }

    private static boolean doGetValidateUrlTokens(String[] urlTokens) {
        if (urlTokens.length > 2) {
            return false;
        }

        boolean isIdValid = PathInfoUtils.parseId(urlTokens[0]) != null;

        if (urlTokens.length == 1 && isIdValid) {
            return true;
        }

        return isIdValid && urlTokens[1].equals("sessions");
    }

    private static boolean doPostValidateCustomerParams(Map<String, Object> customerParams) {
        return !customerParams.isEmpty() && customerParams.containsKey("name") && customerParams.containsKey("surname");
    }

    private static boolean doPostValidateUrlTokensForBooking(String[] urlTokens) {
        return urlTokens.length == 3
                && PathInfoUtils.parseId(urlTokens[0]) != null
                && urlTokens[1].equals("sessions")
                && PathInfoUtils.parseId(urlTokens[2]) != null;
    }

    private static boolean doPutValidateCustomerParams(Map<String, Object> customerParams) {
        return !customerParams.isEmpty()
                && customerParams.containsKey("name")
                && customerParams.containsKey("surname");
    }

    private static boolean doDeleteValidateUrlTokens(String[] urlTokens) {
        if (urlTokens.length > 3) {
            return false;
        }

        boolean isCustomerIdValid = PathInfoUtils.parseId(urlTokens[0]) != null;

        if (urlTokens.length == 1 && isCustomerIdValid) {
            return true;
        }

        if (urlTokens.length == 2) {
            return false;
        }

        boolean isSessionIdValid = PathInfoUtils.parseId(urlTokens[2]) != null;

        return isCustomerIdValid && urlTokens[1].equals("sessions") && isSessionIdValid;
    }

    private void doGetFindAll(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setContentType("text/plain");
        resp.getWriter().write(customerService.findAll().toString());
    }

    private void doGetFindById(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long customerId = PathInfoUtils.parseId(urlTokens[0]);
        if (customerId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CustomerDto foundCustomer = customerService.findById(customerId);
        if (foundCustomer == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setContentType("text/plain");
        resp.getWriter().write(foundCustomer.toString());
    }

    private void doGetFindCustomerSessions(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long customerId = PathInfoUtils.parseId(urlTokens[0]);
        if (customerId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<SessionDto> customerSessions = customerService.findCustomerSessions(customerId);
        if (customerSessions == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setContentType("text/plain");
        resp.getWriter().write(customerSessions.toString());
    }

    private void doPostCreateCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> customerParams = JsonUtils.toMap(req.getReader());
        if (doPostValidateCustomerParams(customerParams)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String name = (String) customerParams.get("name");
        String surname = (String) customerParams.get("surname");

        Long createdCustomerId = customerService.create(new CustomerDto(null, name, surname, null));

        if (createdCustomerId == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("text/plain");
        resp.getWriter().write(createdCustomerId.toString());
    }

    private void doPostBookSession(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long sessionId = PathInfoUtils.parseId(urlTokens[2]);
        Long customerId = PathInfoUtils.parseId(urlTokens[0]);

        Long bookingId = bookingService.bookSession(new BookingDto(sessionId, customerId));

        if (bookingId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("text/plain");
        resp.getWriter().write(bookingId.toString());
    }

    private void doDeleteRemoveCustomer(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long customerId = PathInfoUtils.parseId(urlTokens[0]);
        if (customerId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (customerService.removeById(customerId)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write("Successful");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doDeleteUnbookSession(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long sessionId = PathInfoUtils.parseId(urlTokens[2]);
        Long customerId = PathInfoUtils.parseId(urlTokens[0]);

        if (bookingService.unbookSession(new BookingDto(sessionId, customerId))) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write("Successful");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
