package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.repository.MovieRepository;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryImplTest extends DatabaseTestExtension {
    private static final MovieRepository<Long> REPOSITORY = new MovieRepositoryImpl();

    @Test
    void whenSaved_thenReturnedWithId() {
        Movie movie = new Movie("example");

        assertNotNull(REPOSITORY.save(movie));
    }

    @Test
    void whenSaved_thenFoundById() {
        Movie movie = new Movie("example");

        Long id = REPOSITORY.save(movie);

        assertNotNull(REPOSITORY.findById(id));

    }

    @Test
    void whenSaved_thenCountIncreased() {
        int initialCount = REPOSITORY.findAll().size();

        Movie movie = new Movie("example");
        REPOSITORY.save(movie);

        assertTrue(REPOSITORY.findAll().size() > initialCount);

    }

    @Test
    void givenSaved_whenDeleted_thenReturnedTrue() {
        Movie movie = new Movie("example");
        Long movieId = REPOSITORY.save(movie);

        assertTrue(REPOSITORY.deleteById(movieId));
    }

    @Test
    void whenDeletedNotPresent_thenReturnedFalse() {
        Long id = Long.MAX_VALUE;

        assertFalse(REPOSITORY.deleteById(id));
    }

    @Test
    void givenSaved_whenDeleted_thenCountDecreased() {
        Movie movie = new Movie("example");
        Long movieId = REPOSITORY.save(movie);
        int initialCount = REPOSITORY.findAll().size();

        REPOSITORY.deleteById(movieId);

        assertTrue(REPOSITORY.findAll().size() < initialCount);

    }

    @Test
    void givenSaved_whenFoundAll_thenReturnsCorrectCount() {
        int initialCount = REPOSITORY.findAll().size();
        REPOSITORY.save(new Movie());

        assertEquals(initialCount + 1, REPOSITORY.findAll().size());
    }

    @Test
    void givenSaved_whenDeleted_thenNotFoundById() {
        Movie movie = new Movie("example");
        Long movieId = REPOSITORY.save(movie);

        REPOSITORY.deleteById(movieId);

        assertNull(REPOSITORY.findById(movieId));

    }

    @Test
    void whenUpdatedNotPresent_thenReturnsFalse() {
        Movie movie = new Movie(Long.MAX_VALUE, "example");

        assertFalse(REPOSITORY.update(movie));
    }

    @Test
    void givenSaved_whenUpdated_thenReturnedWithNewAttributes() {
        Movie movie = new Movie("example");
        Long id = REPOSITORY.save(movie);

        REPOSITORY.update(new Movie(id, "other"));
        Movie updatedMovie = REPOSITORY.findById(id);

        assertNotEquals(movie.getTitle(), updatedMovie.getTitle());
    }

    @Test
    void givenNullFields_whenSaved_thenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            REPOSITORY.save(new Movie(null, null));
        });
    }

    @Test
    void givenNullTitle_whenUpdated_thenReturnedMovieWithNullTitle() {
        Movie movie = new Movie("example");
        Long id = REPOSITORY.save(movie);
        Movie savedMovie = REPOSITORY.findById(id);
        savedMovie.setTitle(null);

        REPOSITORY.update(savedMovie);
        Movie updatedMovie = REPOSITORY.findById(savedMovie.getId());

        assertNull(updatedMovie.getTitle());
    }
}
