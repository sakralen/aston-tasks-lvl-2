package edu.sakralen.task2.servlet;

import edu.sakralen.task2.service.MovieService;
import edu.sakralen.task2.service.dto.MovieDto;
import edu.sakralen.task2.service.impl.MovieServiceImpl;
import edu.sakralen.task2.util.PathInfoUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/movies/*")
public class MovieServlet extends HttpServlet {
    private final MovieService<Long> movieService;

    public MovieServlet() {
        this.movieService = new MovieServiceImpl();
    }

    public MovieServlet(MovieService<Long> movieService) {
        this.movieService = movieService;
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

        doGetFindById(urlTokens, resp);
    }

    private void doGetFindAll(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setContentType("text/plain");
        resp.getWriter().write(movieService.findAll().toString());
    }

    private static boolean doGetValidateUrlTokens(String[] urlTokens) {
        return urlTokens.length == 1 && PathInfoUtils.parseId(urlTokens[0]) != null;
    }

    private void doGetFindById(String[] urlTokens, HttpServletResponse resp) throws IOException {
        Long movieId = PathInfoUtils.parseId(urlTokens[0]);
        if (movieId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MovieDto foundMovie = movieService.findById(movieId);
        if (foundMovie == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setContentType("text/plain");
        resp.getWriter().write(foundMovie.toString());
    }
}
