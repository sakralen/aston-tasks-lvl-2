package edu.sakralen.task2.servlet;

import edu.sakralen.task2.service.BookingService;
import edu.sakralen.task2.service.SessionService;
import edu.sakralen.task2.service.dto.BookingDto;
import edu.sakralen.task2.service.dto.SessionDto;
import edu.sakralen.task2.service.impl.BookingServiceImpl;
import edu.sakralen.task2.service.impl.SessionServiceImpl;
import edu.sakralen.task2.util.PathInfoUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/sessions/*")
public class SessionServlet extends HttpServlet {
    private final BookingService<Long> bookingService;
    private final SessionService<Long> sessionService;

    public SessionServlet() {
        this.bookingService = new BookingServiceImpl();
        this.sessionService = new SessionServiceImpl();
    }

    public SessionServlet(BookingService<Long> bookingService, SessionService<Long> sessionService) {
        this.bookingService = bookingService;
        this.sessionService = sessionService;
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

        doGetCountCustomers(urlTokens, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (!validateUrlTokensForBookingOperations(urlTokens)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long sessionId = PathInfoUtils.parseId(urlTokens[0]);
        Long customerId = PathInfoUtils.parseId(urlTokens[2]);

        Long bookingId = bookingService.bookSession(new BookingDto(sessionId, customerId));

        if (bookingId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("text/plain");
        resp.getWriter().write(bookingId.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (PathInfoUtils.isPathInfoEmpty(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] urlTokens = pathInfo.substring(1).split("/");

        if (!validateUrlTokensForBookingOperations(urlTokens)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long sessionId = PathInfoUtils.parseId(urlTokens[0]);
        Long customerId = PathInfoUtils.parseId(urlTokens[2]);

        if (bookingService.unbookSession(new BookingDto(sessionId, customerId))) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write("Successful");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private static boolean doGetValidateUrlTokens(String[] urlTokens) {
        if (urlTokens.length > 2) {
            return false;
        }

        boolean isSessionIdValid = PathInfoUtils.parseId(urlTokens[0]) != null;

        if (urlTokens.length == 1) {
            return isSessionIdValid;
        }

        return isSessionIdValid && urlTokens[1].equals("customers");
    }

    private static boolean validateUrlTokensForBookingOperations(String[] urlTokens) {
        if (urlTokens.length != 3) {
            return false;
        }

        boolean isCustomerIdValid = PathInfoUtils.parseId(urlTokens[0]) != null;
        boolean isSessionIdValid = PathInfoUtils.parseId(urlTokens[2]) != null;

        return isCustomerIdValid && urlTokens[1].equals("customers") && isSessionIdValid;
    }

    private void doGetCountCustomers(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long sessionId = PathInfoUtils.parseId(urlTokens[0]);
        if (sessionId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long customersCount = sessionService.getCustomersCountAtSession(sessionId);
        if (customersCount == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.getWriter().write(customersCount.toString());
    }

    private void doGetFindAll(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.getWriter().write(sessionService.findAll().toString());
    }

    private void doGetFindById(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long sessionId = PathInfoUtils.parseId(urlTokens[0]);
        if (sessionId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SessionDto foundSession = sessionService.findById(sessionId);
        if (foundSession == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.getWriter().write(foundSession.toString());
    }
}
