package edu.sakralen.task2.servlet;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.service.MovieService;
import edu.sakralen.task2.service.dto.MovieDto;
import edu.sakralen.task2.service.dto.mapper.MovieDtoMapper;
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
class MovieServletTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MovieService<Long> movieService;
    @InjectMocks
    private MovieServlet movieServlet;

    @Test
    void givenEmptyPathInfo_whenDoGet_thenWritesMovieDtoList() throws ServletException, IOException {
        Movie movie = new Movie();
        MovieDto movieDto = Mappers.getMapper(MovieDtoMapper.class).toDto(movie);
        List<MovieDto> movieDtos = List.of(movieDto);

        Mockito.when(req.getPathInfo()).thenReturn("/");
        Mockito.when(movieService.findAll()).thenReturn(movieDtos);
        Mockito.when(resp.getWriter()).thenReturn(printWriter);

        movieServlet.doGet(req, resp);

        Mockito.verify(resp).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(resp).setContentType("text/plain");
        Mockito.verify(printWriter).write(movieDtos.toString());
    }

    @Test
    void givenIncorrectUrl_whenDoGet_thenSendsBadRequest() throws ServletException, IOException {
        Mockito.when(req.getPathInfo()).thenReturn("/incorrect");

        movieServlet.doGet(req, resp);

        Mockito.verify(resp).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void givenUrlWithIOfExistingMovie_whenDoGet_thenWritesMovie() throws IOException, ServletException {
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        MovieDto movieDto = Mappers.getMapper(MovieDtoMapper.class).toDto(movie);

        Mockito.when(req.getPathInfo()).thenReturn("/1");
        Mockito.when(movieService.findById(movieId)).thenReturn(movieDto);
        Mockito.when(resp.getWriter()).thenReturn(printWriter);

        movieServlet.doGet(req, resp);

        Mockito.verify(resp).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(resp).setContentType("text/plain");
        Mockito.verify(printWriter).write(movieDto.toString());
    }

    @Test
    void givenUrlOfNotExistingMovie_thenSendsNotFound() throws ServletException, IOException {
        Long movieId = 1L;

        Mockito.when(req.getPathInfo()).thenReturn("/1");
        Mockito.when(movieService.findById(movieId)).thenReturn(null);

        movieServlet.doGet(req, resp);

        Mockito.verify(resp).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
