package edu.sakralen.task2.repository.impl;

import edu.sakralen.task2.model.Customer;
import edu.sakralen.task2.model.Movie;
import edu.sakralen.task2.model.Session;
import edu.sakralen.task2.model.SessionCustomer;
import edu.sakralen.task2.repository.SessionRepository;
import edu.sakralen.task2.testutil.DatabaseTestExtension;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionRepositoryImplTest extends DatabaseTestExtension {
    private static final SessionRepository<Long> REPOSITORY = new SessionRepositoryImpl();

    @Test
    void whenSaved_thenReturnedWithId() {
        assertNotNull(REPOSITORY.save(new Session()));
    }

    @Test
    void givenSaved_thenFoundById_ReturnsSavedSession() {
        Session session = new Session(LocalDateTime.now(), new BigDecimal("1.75"));
        Long id = REPOSITORY.save(session);
        session.setId(id);

        Session found = REPOSITORY.findById(id);

        assertEquals(session, found);
    }


    @Test
    void givenSaved_whenDeleted_thenReturnedTrue() {
        Long id = REPOSITORY.save(new Session());

        assertTrue(REPOSITORY.deleteById(id));
    }

    @Test
    void whenDeletedNotPresent_thenReturnedFalse() {
        assertFalse(REPOSITORY.deleteById(Long.MAX_VALUE));
    }


    @Test
    void givenSaved_whenDeleted_thenNotFoundById() {
        Long id = REPOSITORY.save(new Session());

        REPOSITORY.deleteById(id);

        assertNull(REPOSITORY.findById(id));
    }

    @Test
    void givenSavedWithMovie_whenFoundWithMovie_thenMoviePresent() {
        Movie movie = new Movie();
        Long movieId = new MovieRepositoryImpl().save(movie);
        movie.setId(movieId);

        Session session = new Session();
        session.setMovie(movie);
        Long sessionId = REPOSITORY.save(session);

        assertNotNull(REPOSITORY.findByIdWithMovie(sessionId).getMovie());
    }

    @Test
    void whenUpdatedNotPresent_thenReturnsFalse() {
        assertFalse(REPOSITORY.update(new Session(Long.MAX_VALUE, LocalDateTime.now(), new BigDecimal("1.75"))));
    }

    @Test
    void givenSaved_whenUpdated_thenReturnedMovieWithNewAttributes() {
        Session inititalSession = new Session(LocalDateTime.now(), new BigDecimal("1.00"));
        Long sessionId = REPOSITORY.save(inititalSession);
        inititalSession.setId(sessionId);

        Session updatedSession = new Session(sessionId, LocalDateTime.now(), new BigDecimal("0.75"));

        REPOSITORY.update(updatedSession);

        assertNotEquals(REPOSITORY.findById(sessionId), inititalSession);
    }

    @Test
    void givenSaved_whenFoundAll_thenReturnsCorrectCount() {
        int initialCount = REPOSITORY.findAll().size();
        REPOSITORY.save(new Session());

        assertEquals(initialCount + 1, REPOSITORY.findAll().size());
    }

    @Test
    void givenSavedSessionWithCustomers_whenCounted_ReturnsCorrectCount() {
        Movie movie = new Movie();
        Long movieId = new MovieRepositoryImpl().save(movie);
        movie.setId(movieId);

        Session session = new Session();
        session.setMovie(movie);
        Long sessionId = REPOSITORY.save(session);

        Customer customer = new Customer();
        Long customerId = new CustomerRepositoryImpl().save(customer);
        customer.setId(customerId);

        SessionCustomer sessionCustomer = new SessionCustomer();
        sessionCustomer.setSession(session);
        sessionCustomer.setCustomer(customer);
        new SessionCustomerRepositoryImpl().save(sessionCustomer);

        assertEquals(1, REPOSITORY.findByIdWitCustomers(sessionId).getCustomers().size());
    }
}
